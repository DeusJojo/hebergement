package com.afpa.hebergement.model.mapper;

import com.afpa.hebergement.model.dto.ContactPersonDTO;
import com.afpa.hebergement.model.entity.ContactPerson;
import com.afpa.hebergement.util.StringUtil;

public class ContactPersonMapper {

    private ContactPersonMapper() {}

    public static ContactPersonDTO mapToContactPersonDto(ContactPerson contactPerson) {
        ContactPersonDTO contactPersonDto = new ContactPersonDTO();
        contactPersonDto.setId(contactPerson.getId());
        contactPersonDto.setName(contactPerson.getName().toUpperCase());
        contactPersonDto.setFirstname(StringUtil.capitalize(contactPerson.getFirstname()));
        contactPersonDto.setPhoneNumberContact(contactPerson.getPhoneNumberContact());
        return contactPersonDto;
    }

    public static ContactPerson mapToContactPerson(ContactPersonDTO contactPersonDto) {
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setId(contactPersonDto.getId());
        contactPerson.setName(contactPersonDto.getName().toLowerCase().trim());
        contactPerson.setFirstname(contactPersonDto.getFirstname().toLowerCase().trim());
        contactPerson.setPhoneNumberContact(StringUtil.deleteSpace(contactPersonDto.getPhoneNumberContact().trim()));
        return contactPerson;
    }
}
