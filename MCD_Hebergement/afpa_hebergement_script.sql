------------------------------------------------------------
--        Script Postgre 
------------------------------------------------------------



------------------------------------------------------------
-- Table: role
------------------------------------------------------------
CREATE TABLE public.role(
	id_role        SERIAL NOT NULL ,
	wording_role   VARCHAR (50) NOT NULL  ,
	CONSTRAINT role_PK PRIMARY KEY (id_role)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: civility
------------------------------------------------------------
CREATE TABLE public.civility(
	id_civility        SERIAL NOT NULL ,
	wording_civility   VARCHAR (50) NOT NULL  ,
	CONSTRAINT civility_PK PRIMARY KEY (id_civility)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: region
------------------------------------------------------------
CREATE TABLE public.region(
	id_region     SERIAL NOT NULL ,
	region_name   VARCHAR (150) NOT NULL ,
	region_code   CHAR (2)  NOT NULL  ,
	CONSTRAINT region_PK PRIMARY KEY (id_region)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: department
------------------------------------------------------------
CREATE TABLE public.department(
	id_department     SERIAL NOT NULL ,
	department_name   VARCHAR (150) NOT NULL ,
	department_code   VARCHAR (3) NOT NULL ,
	id_region         INT  NOT NULL  ,
	CONSTRAINT department_PK PRIMARY KEY (id_department)

	,CONSTRAINT department_region_FK FOREIGN KEY (id_region) REFERENCES public.region(id_region)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: city
------------------------------------------------------------
CREATE TABLE public.city(
	id_city         SERIAL NOT NULL ,
	city_name       VARCHAR (255) NOT NULL ,
	insee_code      CHAR (5)  NOT NULL ,
	id_department   INT  NOT NULL  ,
	CONSTRAINT city_PK PRIMARY KEY (id_city)

	,CONSTRAINT city_department_FK FOREIGN KEY (id_department) REFERENCES public.department(id_department)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: afpa_center
------------------------------------------------------------
CREATE TABLE public.afpa_center(
	id_afpa_center           SERIAL NOT NULL ,
	center_name              VARCHAR (255) NOT NULL ,
	address_center           VARCHAR (255) NOT NULL ,
	complement_addr_center   VARCHAR (255)  ,
	siren                    VARCHAR (25) NOT NULL ,
	tva_number               VARCHAR (13) NOT NULL ,
	center_phone_num         VARCHAR (20) NOT NULL ,
	fax_center               VARCHAR (40)  ,
	id_city                  INT  NOT NULL  ,
	CONSTRAINT afpa_center_PK PRIMARY KEY (id_afpa_center)

	,CONSTRAINT afpa_center_city_FK FOREIGN KEY (id_city) REFERENCES public.city(id_city)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: app_user
------------------------------------------------------------
CREATE TABLE public.app_user(
	id_user                   SERIAL NOT NULL ,
	beneficiary_number        VARCHAR (50) NOT NULL ,
	name                      VARCHAR (150) NOT NULL ,
	firstname                 VARCHAR (150) NOT NULL ,
	email                     VARCHAR (255) NOT NULL ,
	password                  VARCHAR (150) NOT NULL ,
	birthdate                 DATE  NOT NULL ,
	birthplace                CHAR (5)  NOT NULL ,
	phone_number              VARCHAR (20) NOT NULL ,
	is_blacklisted            BOOL  NOT NULL ,
	registration_date         DATE  NOT NULL ,
	ip_registration           VARCHAR (50)  ,
	tracker_registration      VARCHAR (255)  ,
	address_user              VARCHAR (255) NOT NULL ,
	complement_user_address   VARCHAR (255)  ,
	reset_password_token      VARCHAR (100)  ,
	id_role                   INT  NOT NULL ,
	id_civility               INT  NOT NULL ,
	id_city                   INT  NOT NULL ,
	id_afpa_center            INT  NOT NULL  ,
	CONSTRAINT app_user_PK PRIMARY KEY (id_user)

	,CONSTRAINT app_user_role_FK FOREIGN KEY (id_role) REFERENCES public.role(id_role)
	,CONSTRAINT app_user_civility0_FK FOREIGN KEY (id_civility) REFERENCES public.civility(id_civility)
	,CONSTRAINT app_user_city1_FK FOREIGN KEY (id_city) REFERENCES public.city(id_city)
	,CONSTRAINT app_user_afpa_center2_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: contact_person
------------------------------------------------------------
CREATE TABLE public.contact_person(
	id_contact_person      SERIAL NOT NULL ,
	name                   VARCHAR (255) NOT NULL ,
	firstname              VARCHAR (255) NOT NULL ,
	phone_number_contact   VARCHAR (20) NOT NULL ,
	id_user                INT  NOT NULL  ,
	CONSTRAINT contact_person_PK PRIMARY KEY (id_contact_person)

	,CONSTRAINT contact_person_app_user_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: intendant
------------------------------------------------------------
CREATE TABLE public.intendant(
	id_intendant           SERIAL NOT NULL ,
	start_date_intendant   DATE   ,
	end_date_intendant     DATE   ,
	id_user                INT  NOT NULL  ,
	CONSTRAINT intendant_PK PRIMARY KEY (id_intendant)

	,CONSTRAINT intendant_app_user_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: shedules
------------------------------------------------------------
CREATE TABLE public.shedules(
	id_shedules       SERIAL NOT NULL ,
	work_date         DATE  NOT NULL ,
	start_morning     TIMETZ   ,
	end_morning       TIMETZ   ,
	start_afternoon   TIMETZ   ,
	end_afternoon     TIMETZ   ,
	id_intendant      INT  NOT NULL  ,
	CONSTRAINT shedules_PK PRIMARY KEY (id_shedules)

	,CONSTRAINT shedules_intendant_FK FOREIGN KEY (id_intendant) REFERENCES public.intendant(id_intendant)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: appointment
------------------------------------------------------------
CREATE TABLE public.appointment(
	id_appointment     SERIAL NOT NULL ,
	date_appointment   DATE  NOT NULL ,
	reason             VARCHAR (100) NOT NULL ,
	id_intendant       INT  NOT NULL ,
	id_user            INT  NOT NULL  ,
	CONSTRAINT appointment_PK PRIMARY KEY (id_appointment)

	,CONSTRAINT appointment_intendant_FK FOREIGN KEY (id_intendant) REFERENCES public.intendant(id_intendant)
	,CONSTRAINT appointment_app_user0_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: message
------------------------------------------------------------
CREATE TABLE public.message(
	id_message     SERIAL NOT NULL ,
	content        VARCHAR (2000)  NOT NULL ,
	message_date   DATE  NOT NULL ,
	id_intendant   INT  NOT NULL ,
	id_user        INT  NOT NULL  ,
	CONSTRAINT message_PK PRIMARY KEY (id_message)

	,CONSTRAINT message_intendant_FK FOREIGN KEY (id_intendant) REFERENCES public.intendant(id_intendant)
	,CONSTRAINT message_app_user0_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: rent
------------------------------------------------------------
CREATE TABLE public.rent(
	id_rent          SERIAL NOT NULL ,
	frequency        VARCHAR (5) NOT NULL ,
	amount           DECIMAL (15,3) NOT NULL ,
	id_afpa_center   INT  NOT NULL  ,
	CONSTRAINT rent_PK PRIMARY KEY (id_rent)

	,CONSTRAINT rent_afpa_center_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: deposite_type
------------------------------------------------------------
CREATE TABLE public.deposite_type(
	id_deposite_type        SERIAL NOT NULL ,
	wording_deposite_type   VARCHAR (10) NOT NULL  ,
	CONSTRAINT deposite_type_PK PRIMARY KEY (id_deposite_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: financier
------------------------------------------------------------
CREATE TABLE public.financier(
	id_financier         SERIAL NOT NULL ,
	covering_cost        DECIMAL (15,3) NOT NULL ,
	log_name             VARCHAR (255) NOT NULL ,
	url_logo_financier   VARCHAR (255)   ,
	CONSTRAINT financier_PK PRIMARY KEY (id_financier)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: domaine
------------------------------------------------------------
CREATE TABLE public.domaine(
	id_domaine    SERIAL NOT NULL ,
	lib_domaine   VARCHAR (255) NOT NULL ,
	grn           CHAR (3)    ,
	CONSTRAINT domaine_PK PRIMARY KEY (id_domaine)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: formation
------------------------------------------------------------
CREATE TABLE public.formation(
	id_formation     SERIAL NOT NULL ,
	formation_name   VARCHAR (200) NOT NULL ,
	id_domaine       INT  NOT NULL  ,
	CONSTRAINT formation_PK PRIMARY KEY (id_formation)

	,CONSTRAINT formation_domaine_FK FOREIGN KEY (id_domaine) REFERENCES public.domaine(id_domaine)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: session_formation
------------------------------------------------------------
CREATE TABLE public.session_formation(
	id_session           SERIAL NOT NULL ,
	start_date_session   DATE  NOT NULL ,
	end_date_session     DATE  NOT NULL ,
	offer_number         VARCHAR (20) NOT NULL ,
	id_formation         INT  NOT NULL  ,
	CONSTRAINT session_formation_PK PRIMARY KEY (id_session)

	,CONSTRAINT session_formation_formation_FK FOREIGN KEY (id_formation) REFERENCES public.formation(id_formation)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: incident_type
------------------------------------------------------------
CREATE TABLE public.incident_type(
	id_incident_type   SERIAL NOT NULL ,
	wording_incident   VARCHAR (30) NOT NULL  ,
	CONSTRAINT incident_type_PK PRIMARY KEY (id_incident_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: floor
------------------------------------------------------------
CREATE TABLE public.floor(
	id_floor       SERIAL NOT NULL ,
	number_floor   INT  NOT NULL ,
	is_for_women   BOOL  NOT NULL  ,
	CONSTRAINT floor_PK PRIMARY KEY (id_floor)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: document_type
------------------------------------------------------------
CREATE TABLE public.document_type(
	id_document_type   SERIAL NOT NULL ,
	wording_document   VARCHAR (100) NOT NULL  ,
	CONSTRAINT document_type_PK PRIMARY KEY (id_document_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: document_center
------------------------------------------------------------
CREATE TABLE public.document_center(
	id_document_center   SERIAL NOT NULL ,
	document_name        VARCHAR (255) NOT NULL ,
	document_url         VARCHAR (255) NOT NULL ,
	date_depo_doc        DATE  NOT NULL ,
	doc_commentary       VARCHAR (2000)   ,
	id_afpa_center       INT  NOT NULL ,
	id_document_type     INT  NOT NULL  ,
	CONSTRAINT document_center_PK PRIMARY KEY (id_document_center)

	,CONSTRAINT document_center_afpa_center_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
	,CONSTRAINT document_center_document_type0_FK FOREIGN KEY (id_document_type) REFERENCES public.document_type(id_document_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: work_type
------------------------------------------------------------
CREATE TABLE public.work_type(
	id_work_type        SERIAL NOT NULL ,
	wording_work_type   VARCHAR (100) NOT NULL  ,
	CONSTRAINT work_type_PK PRIMARY KEY (id_work_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: payment_type
------------------------------------------------------------
CREATE TABLE public.payment_type(
	id_payment_date        SERIAL NOT NULL ,
	wording_payment_type   VARCHAR (20) NOT NULL  ,
	CONSTRAINT payment_type_PK PRIMARY KEY (id_payment_date)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: alert
------------------------------------------------------------
CREATE TABLE public.alert(
	id_alert           SERIAL NOT NULL ,
	wording_alert      VARCHAR (15) NOT NULL ,
	commentary_alert   VARCHAR (2000)  NOT NULL ,
	date_alert         DATE  NOT NULL ,
	is_archived        BOOL  NOT NULL ,
	id_afpa_center     INT  NOT NULL  ,
	CONSTRAINT alert_PK PRIMARY KEY (id_alert)

	,CONSTRAINT alert_afpa_center_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: document_user
------------------------------------------------------------
CREATE TABLE public.document_user(
	id_document        SERIAL NOT NULL ,
	document_title     VARCHAR (50) NOT NULL ,
	document_url       VARCHAR (255) NOT NULL ,
	date_depot_doc     DATE  NOT NULL ,
	id_user            INT  NOT NULL ,
	id_document_type   INT  NOT NULL ,
	id_afpa_center     INT  NOT NULL  ,
	CONSTRAINT document_user_PK PRIMARY KEY (id_document)

	,CONSTRAINT document_user_app_user_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
	,CONSTRAINT document_user_document_type0_FK FOREIGN KEY (id_document_type) REFERENCES public.document_type(id_document_type)
	,CONSTRAINT document_user_afpa_center1_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: guarentee_type
------------------------------------------------------------
CREATE TABLE public.guarentee_type(
	id_guarrentee_type   SERIAL NOT NULL ,
	wording_guarentee    VARCHAR (50) NOT NULL  ,
	CONSTRAINT guarentee_type_PK PRIMARY KEY (id_guarrentee_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: guarantee
------------------------------------------------------------
CREATE TABLE public.guarantee(
	id_guarantee         SERIAL NOT NULL ,
	amount               DECIMAL (15,3) NOT NULL ,
	id_afpa_center       INT  NOT NULL ,
	id_guarrentee_type   INT  NOT NULL  ,
	CONSTRAINT guarantee_PK PRIMARY KEY (id_guarantee)

	,CONSTRAINT guarantee_afpa_center_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
	,CONSTRAINT guarantee_guarentee_type0_FK FOREIGN KEY (id_guarrentee_type) REFERENCES public.guarentee_type(id_guarrentee_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: deposite
------------------------------------------------------------
CREATE TABLE public.deposite(
	id_deposite        SERIAL NOT NULL ,
	deposite_date      DATE  NOT NULL ,
	back_depo_date     DATE   ,
	id_guarantee       INT  NOT NULL ,
	id_user            INT  NOT NULL ,
	id_deposite_type   INT  NOT NULL  ,
	CONSTRAINT deposite_PK PRIMARY KEY (id_deposite)

	,CONSTRAINT deposite_guarantee_FK FOREIGN KEY (id_guarantee) REFERENCES public.guarantee(id_guarantee)
	,CONSTRAINT deposite_app_user0_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
	,CONSTRAINT deposite_deposite_type1_FK FOREIGN KEY (id_deposite_type) REFERENCES public.deposite_type(id_deposite_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: room
------------------------------------------------------------
CREATE TABLE public.room(
	id_room             SERIAL NOT NULL ,
	room_number         INT  NOT NULL ,
	room_key_number     INT  NOT NULL ,
	is_usable           BOOL  NOT NULL ,
	badge_number        INT  NOT NULL ,
	fridge_key_number   INT  NOT NULL ,
	is_reserved         BOOL  NOT NULL ,
	id_deposite         INT   ,
	id_floor            INT  NOT NULL ,
	id_afpa_center      INT  NOT NULL  ,
	CONSTRAINT room_PK PRIMARY KEY (id_room)

	,CONSTRAINT room_deposite_FK FOREIGN KEY (id_deposite) REFERENCES public.deposite(id_deposite)
	,CONSTRAINT room_floor0_FK FOREIGN KEY (id_floor) REFERENCES public.floor(id_floor)
	,CONSTRAINT room_afpa_center1_FK FOREIGN KEY (id_afpa_center) REFERENCES public.afpa_center(id_afpa_center)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: leaseContract
------------------------------------------------------------
CREATE TABLE public.leaseContract(
	id_lease           SERIAL NOT NULL ,
	start_date_lease   DATE  NOT NULL ,
	end_date_lease     DATE  NOT NULL ,
	is_present         BOOL  NOT NULL ,
	id_user            INT  NOT NULL ,
	id_room            INT  NOT NULL  ,
	CONSTRAINT leaseContract_PK PRIMARY KEY (id_lease)

	,CONSTRAINT leaseContract_app_user_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
	,CONSTRAINT leaseContract_room0_FK FOREIGN KEY (id_room) REFERENCES public.room(id_room)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: work_demand
------------------------------------------------------------
CREATE TABLE public.work_demand(
	id_work_demand       SERIAL NOT NULL ,
	demand_description   VARCHAR (2000)  NOT NULL ,
	img_demand           VARCHAR (100)  ,
	id_lease             INT  NOT NULL  ,
	CONSTRAINT work_demand_PK PRIMARY KEY (id_work_demand)

	,CONSTRAINT work_demand_leaseContract_FK FOREIGN KEY (id_lease) REFERENCES public.leaseContract(id_lease)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: incident
------------------------------------------------------------
CREATE TABLE public.incident(
	id_incident        SERIAL NOT NULL ,
	incident_date      DATE  NOT NULL ,
	commentary_user    VARCHAR (2000)   ,
	commentary_admin   VARCHAR (2000)   ,
	img_incident       VARCHAR (100)  ,
	is_closed          BOOL  NOT NULL ,
	id_lease           INT  NOT NULL ,
	id_incident_type   INT  NOT NULL  ,
	CONSTRAINT incident_PK PRIMARY KEY (id_incident)

	,CONSTRAINT incident_leaseContract_FK FOREIGN KEY (id_lease) REFERENCES public.leaseContract(id_lease)
	,CONSTRAINT incident_incident_type0_FK FOREIGN KEY (id_incident_type) REFERENCES public.incident_type(id_incident_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: warning
------------------------------------------------------------
CREATE TABLE public.warning(
	id_warning     SERIAL NOT NULL ,
	warning_date   DATE  NOT NULL ,
	commentary     VARCHAR (2000)   ,
	id_incident    INT  NOT NULL ,
	id_user        INT  NOT NULL  ,
	CONSTRAINT warning_PK PRIMARY KEY (id_warning)

	,CONSTRAINT warning_incident_FK FOREIGN KEY (id_incident) REFERENCES public.incident(id_incident)
	,CONSTRAINT warning_app_user0_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: work
------------------------------------------------------------
CREATE TABLE public.work(
	id_work           SERIAL NOT NULL ,
	start_work        DATE  NOT NULL ,
	end_work          DATE   ,
	work_commentary   VARCHAR (2000)   ,
	id_work_type      INT  NOT NULL ,
	id_room           INT  NOT NULL  ,
	CONSTRAINT work_PK PRIMARY KEY (id_work)

	,CONSTRAINT work_work_type_FK FOREIGN KEY (id_work_type) REFERENCES public.work_type(id_work_type)
	,CONSTRAINT work_room0_FK FOREIGN KEY (id_room) REFERENCES public.room(id_room)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: absence
------------------------------------------------------------
CREATE TABLE public.absence(
	id_absence       SERIAL NOT NULL ,
	start_abs_date   DATE  NOT NULL ,
	end_abs_date     DATE  NOT NULL ,
	motive           VARCHAR (2000)   ,
	id_lease         INT  NOT NULL  ,
	CONSTRAINT absence_PK PRIMARY KEY (id_absence)

	,CONSTRAINT absence_leaseContract_FK FOREIGN KEY (id_lease) REFERENCES public.leaseContract(id_lease)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: bill
------------------------------------------------------------
CREATE TABLE public.bill(
	id_bill             SERIAL NOT NULL ,
	bill_number         VARCHAR (10) NOT NULL ,
	total_amount        DECIMAL (15,3) NOT NULL ,
	bill_date           DATE  NOT NULL ,
	is_payed            BOOL  NOT NULL ,
	payment_date_bill   DATE   ,
	id_lease            INT  NOT NULL  ,
	CONSTRAINT bill_PK PRIMARY KEY (id_bill)

	,CONSTRAINT bill_leaseContract_FK FOREIGN KEY (id_lease) REFERENCES public.leaseContract(id_lease)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: inventory_type
------------------------------------------------------------
CREATE TABLE public.inventory_type(
	id_inventory_type   SERIAL NOT NULL ,
	wording_inventory   VARCHAR (10) NOT NULL  ,
	CONSTRAINT inventory_type_PK PRIMARY KEY (id_inventory_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: inventory
------------------------------------------------------------
CREATE TABLE public.inventory(
	id_inventory         SERIAL NOT NULL ,
	inventory_date       DATE  NOT NULL ,
	commentary           VARCHAR (2000)   ,
	deducted_guarantee   DECIMAL (15,3)  ,
	id_lease             INT  NOT NULL ,
	id_inventory_type    INT  NOT NULL  ,
	CONSTRAINT inventory_PK PRIMARY KEY (id_inventory)

	,CONSTRAINT inventory_leaseContract_FK FOREIGN KEY (id_lease) REFERENCES public.leaseContract(id_lease)
	,CONSTRAINT inventory_inventory_type0_FK FOREIGN KEY (id_inventory_type) REFERENCES public.inventory_type(id_inventory_type)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: reservation_motive
------------------------------------------------------------
CREATE TABLE public.reservation_motive(
	id_reservation_motive   SERIAL NOT NULL ,
	motive                  VARCHAR (20) NOT NULL  ,
	CONSTRAINT reservation_motive_PK PRIMARY KEY (id_reservation_motive)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: reservation
------------------------------------------------------------
CREATE TABLE public.reservation(
	id_reservation          SERIAL NOT NULL ,
	start_date              DATE  NOT NULL ,
	end_date                DATE  NOT NULL ,
	id_room                 INT  NOT NULL ,
	id_reservation_motive   INT  NOT NULL  ,
	CONSTRAINT reservation_PK PRIMARY KEY (id_reservation)

	,CONSTRAINT reservation_room_FK FOREIGN KEY (id_room) REFERENCES public.room(id_room)
	,CONSTRAINT reservation_reservation_motive0_FK FOREIGN KEY (id_reservation_motive) REFERENCES public.reservation_motive(id_reservation_motive)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: participate
------------------------------------------------------------
CREATE TABLE public.participate(
	id_session     INT  NOT NULL ,
	id_user        INT  NOT NULL ,
	id_financier   INT  NOT NULL  ,
	CONSTRAINT participate_PK PRIMARY KEY (id_session,id_user,id_financier)

	,CONSTRAINT participate_session_formation_FK FOREIGN KEY (id_session) REFERENCES public.session_formation(id_session)
	,CONSTRAINT participate_app_user0_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
	,CONSTRAINT participate_financier1_FK FOREIGN KEY (id_financier) REFERENCES public.financier(id_financier)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: use
------------------------------------------------------------
CREATE TABLE public.use(
	id_bill           INT  NOT NULL ,
	id_payment_date   INT  NOT NULL  ,
	CONSTRAINT use_PK PRIMARY KEY (id_bill,id_payment_date)

	,CONSTRAINT use_bill_FK FOREIGN KEY (id_bill) REFERENCES public.bill(id_bill)
	,CONSTRAINT use_payment_type0_FK FOREIGN KEY (id_payment_date) REFERENCES public.payment_type(id_payment_date)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: see
------------------------------------------------------------
CREATE TABLE public.see(
	id_user    INT  NOT NULL ,
	id_alert   INT  NOT NULL  ,
	CONSTRAINT see_PK PRIMARY KEY (id_user,id_alert)

	,CONSTRAINT see_app_user_FK FOREIGN KEY (id_user) REFERENCES public.app_user(id_user)
	,CONSTRAINT see_alert0_FK FOREIGN KEY (id_alert) REFERENCES public.alert(id_alert)
)WITHOUT OIDS;



