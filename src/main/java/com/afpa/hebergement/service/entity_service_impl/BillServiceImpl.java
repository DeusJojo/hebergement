package com.afpa.hebergement.service.entity_service_impl;

import com.afpa.hebergement.exception.CreationException;
import com.afpa.hebergement.exception.InternalServerException;
import com.afpa.hebergement.exception.ResourceNotFoundException;
import com.afpa.hebergement.model.dto.BillDTO;
import com.afpa.hebergement.model.dto.BillFormDTO;
import com.afpa.hebergement.model.entity.*;
import com.afpa.hebergement.model.mapper.BillMapper;
import com.afpa.hebergement.model.repository.BillRepository;
import com.afpa.hebergement.model.repository.LeaseContractRepository;
import com.afpa.hebergement.model.repository.PaymentTypeRepository;
import com.afpa.hebergement.model.repository.RentRepository;
import com.afpa.hebergement.service.entity_service.BillService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillServiceImpl  implements BillService {

    private final BillRepository billRepository;
    private final LeaseContractRepository leaseContractRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final RentRepository rentRepository;



    @Override
    @Transactional
    public BillDTO create(BillFormDTO billFormDTO) {
        // Retrieve the lease contract by ID
        LeaseContract leaseContract = leaseContractRepository.findById(billFormDTO.getIdLease())
                .orElseThrow(() -> new ResourceNotFoundException("Lease contract not found"));

        // Retrieve the user associated with the lease contract
        AppUser user = leaseContract.getIdUser();

        // Retrieve the rent associated with the lease contract
        Rent rent = leaseContract.getIdRent();

        // Retrieve the Afpa center from the room
        AfpaCenter afpaCenter = rent.getIdAfpaCenter();


        // Calculate the total amount based on rent frequency
        BigDecimal totalAmount = calculateTotalAmount(rent.getAmount(), rent.getFrequency());

        // Create a new bill
        Bill bill = new Bill();
        bill.setBillNumber(billFormDTO.getBillNumber());
        bill.setTotalAmount(totalAmount);  // Set the total amount from the Rent entity
        bill.setBillDate(LocalDate.now());
        bill.setIsPayed(false);
        bill.setIdLease(leaseContract);

        // Save the bill to the repository
        Bill savedBill = billRepository.save(bill);

        // Generate the PDF for the bill
        String pdfOutputPath = "documents/bills/bill_" + savedBill.getId() + ".pdf";
        try {
            generateBillPdf(savedBill, user, totalAmount,afpaCenter, pdfOutputPath);
        } catch (IOException | DocumentException e) {
            throw new CreationException("Error occurred while generating PDF for bill");
        }

        // Map the saved bill to a DTO and return it
        return BillMapper.mapToBillDTO(savedBill);
    }
    private BigDecimal calculateTotalAmount(BigDecimal baseAmount, String frequency) {
        BigDecimal totalAmount;
        switch (frequency.toLowerCase()) {
            case "daily":
                totalAmount = baseAmount;
                break;
            case "weekly":
                totalAmount = baseAmount.multiply(BigDecimal.valueOf(7));
                break;
            case "monthly":
                totalAmount = baseAmount.multiply(BigDecimal.valueOf(30));
                break;

            default:
                totalAmount = baseAmount;
        }
        return totalAmount;
    }
    private void generateBillPdf(Bill bill, AppUser user, BigDecimal totalAmount, AfpaCenter afpaCenter, String pdfOutputPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfOutputPath));
        document.open();

        // Add content to the PDF document
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        // Add title
        document.add(new Paragraph("Quittance de loyer", boldFont));

        // Add date
        String month = bill.getBillDate().getMonth().toString();
        String year = String.valueOf(bill.getBillDate().getYear());
        document.add(new Paragraph(month + " " + year, font));

        // Add center details
        document.add(new Paragraph("CENTRE " + afpaCenter.getCenterName(), font));
        document.add(new Paragraph(afpaCenter.getAddressCenter(), font));
        document.add(new Paragraph(afpaCenter.getIdCity().getPostcode() + " " + afpaCenter.getIdCity().getCityName(), font));

        // Add bill details
        document.add(new Paragraph("Fait à " + afpaCenter.getIdCity().getCityName() + ", le " + bill.getBillDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), font));

        // Add user details
        document.add(new Paragraph("Reçu de : " + user.getIdCivility().getWordingCivility() + " " + user.getName() + " " + user.getFirstname(), font));

        // Add payment details
        document.add(new Paragraph("Détail du règlement :", boldFont));
        document.add(new Paragraph("Quittance N° : " + bill.getBillNumber(), font));
        document.add(new Paragraph("Total loyer : " + totalAmount.toString() + " euros", font));

        // Add payment types
        Set<String> paymentTypeNames = bill.getPaymentTypes().stream().map(PaymentType::getWordingPaymentType).collect(Collectors.toSet());
        document.add(new Paragraph("Mode de règlement : " + String.join(", ", paymentTypeNames), font));

        // Add payment date
        if (bill.getPaymentDateBill() != null) {
            document.add(new Paragraph("Date du paiement : le " + bill.getPaymentDateBill().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), font));
        } else {
            document.add(new Paragraph("Date du paiement : ", font));
        }

        // Add signature line
        document.add(new Paragraph("Signature", font));

        // Add footer
        document.add(new Paragraph("(En bas de page) Cette quittance annule tous les reçus qui auraient pu être établis précédemment en cas de paiement partiel du montant du présent terme. Elle est à conserver pendant trois ans par le locataire (loi n° 89-462 du 6 juillet 1989 : art. 7-1).", font));

        document.close();
    }

    @Override
    public Optional<BillDTO> getById(Integer id) {
        Bill findBill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        try {
            BillDTO billDTO = BillMapper.mapToBillDTO(findBill);
            return Optional.of(billDTO);
        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("Error occurred to recover Bill  with ID: " + id);
        }
    }


    @Override
    public void deleteById(Integer id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        try {
            billRepository.deleteById(bill.getId());
        } catch (Exception e) {
            // Capture toute exception inattendue et lance une exception avec un message d'erreur
            throw new InternalServerException("An unexpected error occurred while deleting the bill with id: " + id);
        }
    }
}
