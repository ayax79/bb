SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: bb_accesscontrollist; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_accesscontrollist (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint
);


ALTER TABLE public.bb_accesscontrollist OWNER TO blackbox;

--
-- Name: bb_accesscontrollist_bb_application_permission; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_accesscontrollist_bb_application_permission (
    bb_accesscontrollist_guid character varying(255) NOT NULL,
    applicationpermissions_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_accesscontrollist_bb_application_permission OWNER TO blackbox;

--
-- Name: bb_accesscontrollist_bb_role; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_accesscontrollist_bb_role (
    bb_accesscontrollist_guid character varying(255) NOT NULL,
    roles_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_accesscontrollist_bb_role OWNER TO blackbox;

--
-- Name: bb_accesscontrollist_bb_social_permission; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_accesscontrollist_bb_social_permission (
    bb_accesscontrollist_guid character varying(255) NOT NULL,
    socialpermissions_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_accesscontrollist_bb_social_permission OWNER TO blackbox;

--
-- Name: bb_activity; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_activity (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    acknowledged boolean NOT NULL,
    activitytype integer,
    ownerguid character varying(255) NOT NULL,
    ownerownertype integer NOT NULL,
    artifacttype integer NOT NULL,
    aspect double precision NOT NULL,
    assetid character varying(255),
    avatar boolean NOT NULL,
    comment text,
    episodeid character varying(255),
    filename character varying(255),
    location character varying(255),
    loosepic boolean NOT NULL,
    mimetype character varying(100),
    occasionaddress1 character varying(255),
    occasionaddress2 character varying(255),
    occasioncity character varying(255),
    occasioncountry character varying(255),
    occasioncounty character varying(255),
    occasionlatitude bigint,
    occasionlongitude bigint,
    occasionstate character varying(255),
    occasionzipcode character varying(255),
    occasionavatarlocation character varying(255),
    occasiondescription text,
    occasioneventendtime timestamp with time zone,
    occasioneventtime timestamp with time zone,
    occasionurl character varying(255),
    occasionguid character varying(255),
    occasionhostby character varying(255),
    occasiontype integer,
    ownertype integer,
    parentactivitytype integer,
    parentguid character varying(255),
    parentownertype integer,
    parentactivitypostdate timestamp without time zone,
    passwordprotect boolean NOT NULL,
    postdate timestamp with time zone NOT NULL,
    publishtotwitter boolean NOT NULL,
    recipientdepth integer NOT NULL,
    senderavatarimage character varying(255),
    senderdisplayname character varying(255),
    senderprofileimage character varying(255),
    contentsize bigint,
    thumbnaillocation character varying(255),
    virtualgift boolean NOT NULL
);


ALTER TABLE public.bb_activity OWNER TO blackbox;

--
-- Name: bb_affiliate_mapping; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_affiliate_mapping (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    affiliate_guid character varying(255)
);


ALTER TABLE public.bb_affiliate_mapping OWNER TO blackbox;

--
-- Name: bb_affiliate_mapping_bb_user; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_affiliate_mapping_bb_user (
    bb_affiliate_mapping_guid character varying(255) NOT NULL,
    users_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_affiliate_mapping_bb_user OWNER TO blackbox;

--
-- Name: bb_application_permission; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_application_permission (
    adminpermaccess character varying(255),
    adminpermcategory character varying(255),
    adminpermfield character varying(255),
    guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_application_permission OWNER TO blackbox;

--
-- Name: bb_billing_info; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_billing_info (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    address1 character varying(255),
    address2 character varying(255),
    city character varying(100),
    country character varying(100),
    county character varying(255),
    latitude bigint,
    longitude bigint,
    state character varying(100),
    zipcode character varying(50),
    billingphone character varying(255),
    lastamount character varying(255),
    lastbilled timestamp with time zone NOT NULL,
    lastcardnum character varying(255),
    lastcardtype character varying(255),
    lastexpirationdate timestamp with time zone,
    lastresponse character varying(255),
    provider integer,
    providerguid character varying(255),
    user_guid character varying(255) NOT NULL,
    authresponse character varying(255),
    nextbilldate timestamp with time zone
);


ALTER TABLE public.bb_billing_info OWNER TO blackbox;

--
-- Name: bb_bookmark; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_bookmark (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    acknowledged boolean NOT NULL,
    referenceguid character varying(255) NOT NULL,
    referencetype integer NOT NULL,
    type integer,
    owner_guid character varying(255),
    description character varying(255)
);


ALTER TABLE public.bb_bookmark OWNER TO blackbox;

--
-- Name: bb_cartitem; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_cartitem (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    merchant_guid character varying(255) NOT NULL,
    productamount character varying(255),
    amountlocale character varying(255),
    product_guid character varying(255) NOT NULL,
    product_sku character varying(255) NOT NULL,
    total_quantity integer NOT NULL
);


ALTER TABLE public.bb_cartitem OWNER TO blackbox;

--
-- Name: bb_corkboard_image; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_corkboard_image (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    metadataguid character varying(255),
    referenceguid character varying(255) NOT NULL,
    referencetype integer NOT NULL,
    rotation integer,
    scale double precision,
    x integer,
    y integer,
    z integer
);


ALTER TABLE public.bb_corkboard_image OWNER TO blackbox;

--
-- Name: bb_db_meta_data; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_db_meta_data (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    key character varying(255)
);


ALTER TABLE public.bb_db_meta_data OWNER TO blackbox;

--
-- Name: bb_email_capture; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_email_capture (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    email character varying(255)
);


ALTER TABLE public.bb_email_capture OWNER TO blackbox;

--
-- Name: bb_ext_cred; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_ext_cred (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    referenceguid character varying(255) NOT NULL,
    referencetype integer NOT NULL,
    password character varying(255),
    type integer,
    username character varying(255),
    externalkey character varying(255)
);


ALTER TABLE public.bb_ext_cred OWNER TO blackbox;

--
-- Name: bb_gift_layout; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_gift_layout (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    active boolean NOT NULL,
    referenceguid character varying(255) NOT NULL,
    referencetype integer NOT NULL,
    frame character varying(255),
    iconlocation character varying(255),
    shelf integer,
    size integer,
    x integer NOT NULL,
    y integer NOT NULL,
    z integer NOT NULL
);


ALTER TABLE public.bb_gift_layout OWNER TO blackbox;

--
-- Name: bb_group; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_group (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    description character varying(255)
);


ALTER TABLE public.bb_group OWNER TO blackbox;

--
-- Name: bb_ignore; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_ignore (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    ignorerguid character varying(255),
    ignorerownertype integer,
    targetguid character varying(255),
    targetownertype integer,
    type integer NOT NULL
);


ALTER TABLE public.bb_ignore OWNER TO blackbox;

--
-- Name: bb_invoice; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_invoice (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    address1 character varying(255),
    address2 character varying(255),
    city character varying(100),
    country character varying(100),
    county character varying(255),
    state character varying(100),
    zipcode character varying(50),
    cart_guid character varying(255) NOT NULL,
    latitude bigint,
    longitude bigint
);


ALTER TABLE public.bb_invoice OWNER TO blackbox;

--
-- Name: bb_invoice_bb_invoice_split; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_invoice_bb_invoice_split (
    bb_invoice_guid character varying(255) NOT NULL,
    splits_identifier bigint NOT NULL
);


ALTER TABLE public.bb_invoice_bb_invoice_split OWNER TO blackbox;

--
-- Name: bb_invoice_split; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_invoice_split (
    identifier bigint NOT NULL,
    contributionamount character varying(255),
    amountlocale character varying(255),
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    first_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_invoice_split OWNER TO blackbox;

--
-- Name: bb_media_library; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_media_library (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    description character varying(255),
    networktypeenum integer,
    referenceguid character varying(255) NOT NULL,
    referencetype integer NOT NULL,
    type integer NOT NULL
);


ALTER TABLE public.bb_media_library OWNER TO blackbox;

--
-- Name: bb_media_library_bb_media_meta_data; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_media_library_bb_media_meta_data (
    bb_media_library_guid character varying(255) NOT NULL,
    media_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_media_library_bb_media_meta_data OWNER TO blackbox;

--
-- Name: bb_media_meta_data; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_media_meta_data (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    ownerguid character varying(255) NOT NULL,
    ownerownertype integer NOT NULL,
    artifacttype integer NOT NULL,
    aspect double precision NOT NULL,
    assetid character varying(255),
    avatar boolean NOT NULL,
    comment text,
    episodeid character varying(255),
    filename character varying(255) NOT NULL,
    librarymedia boolean,
    location character varying(255),
    loosepic boolean NOT NULL,
    mimetype character varying(100) NOT NULL,
    parentactivitytype integer,
    parentguid character varying(255),
    parentownertype integer,
    parentactivitypostdate timestamp without time zone,
    passwordprotect boolean NOT NULL,
    postdate timestamp with time zone NOT NULL,
    profile boolean NOT NULL,
    recipientdepth integer NOT NULL,
    senderavatarimage character varying(255),
    senderdisplayname character varying(255),
    senderprofileimage character varying(255),
    contentsize bigint NOT NULL,
    thumbnaillocation character varying(255),
    medialibrary_guid character varying(255),
    acknowledged boolean,
    virtualgift boolean
);


ALTER TABLE public.bb_media_meta_data OWNER TO blackbox;

--
-- Name: bb_media_meta_data_bb_media_recipient; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_media_meta_data_bb_media_recipient (
    bb_media_meta_data_guid character varying(255) NOT NULL,
    recipients_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_media_meta_data_bb_media_recipient OWNER TO blackbox;

--
-- Name: bb_media_recipient; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_media_recipient (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    recipientguid character varying(255),
    recipientownertype integer,
    status integer,
    messagemetadata_guid character varying(255)
);


ALTER TABLE public.bb_media_recipient OWNER TO blackbox;

--
-- Name: bb_merchant; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_merchant (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    integrationguid character varying(255) NOT NULL
);


ALTER TABLE public.bb_merchant OWNER TO blackbox;

--
-- Name: bb_merchant_categories; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_merchant_categories (
    bb_merchant_guid character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.bb_merchant_categories OWNER TO blackbox;

--
-- Name: bb_message; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_message (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    body text,
    parentactivitytype integer,
    parentguid character varying(255),
    parentownertype integer,
    parentactivitypostdate timestamp without time zone,
    postdate timestamp with time zone NOT NULL,
    published boolean NOT NULL,
    recipientdepth integer NOT NULL,
    senderavatarimage character varying(255),
    senderdisplayname character varying(255),
    senderprofileimage character varying(255),
    subject character varying(255),
    artifactmetadata_guid character varying(255) NOT NULL,
    acknowledged boolean,
    virtualgift boolean
);


ALTER TABLE public.bb_message OWNER TO blackbox;

--
-- Name: bb_message_meta_data; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_message_meta_data (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    ownerguid character varying(255) NOT NULL,
    ownerownertype integer NOT NULL,
    artifacttype integer NOT NULL,
    customdefinedtype character varying(255),
    customtypeowner character varying(255)
);


ALTER TABLE public.bb_message_meta_data OWNER TO blackbox;

--
-- Name: bb_message_meta_data_bb_message_recipient; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_message_meta_data_bb_message_recipient (
    bb_message_meta_data_guid character varying(255) NOT NULL,
    recipients_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_message_meta_data_bb_message_recipient OWNER TO blackbox;

--
-- Name: bb_message_recipient; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_message_recipient (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    messageguid character varying(255),
    read boolean NOT NULL,
    recipientguid character varying(255),
    recipientownertype integer,
    replydate timestamp with time zone,
    status integer,
    messagemetadata_guid character varying(255)
);


ALTER TABLE public.bb_message_recipient OWNER TO blackbox;

--
-- Name: bb_occasion; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_occasion (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    address1 character varying(255),
    address2 character varying(255),
    city character varying(100),
    country character varying(100),
    county character varying(255),
    state character varying(100),
    zipcode character varying(50),
    avatarlocation character varying(255),
    description text,
    email character varying(255),
    eventendtime timestamp with time zone,
    eventtime timestamp with time zone NOT NULL,
    eventurl character varying(255),
    hostby character varying(255),
    location character varying(255),
    occasionlevel integer,
    guestbringfriendnbr integer NOT NULL,
    guestcanforward boolean NOT NULL,
    guestmaxnbr integer NOT NULL,
    remindersend1daybeforewrsvp boolean NOT NULL,
    remindersend7daysbeforeworsvp boolean NOT NULL,
    remindersendtobbxinbox boolean NOT NULL,
    remindersendviaemail boolean NOT NULL,
    thankcustomnotes character varying(255),
    thanknotessendviabbx boolean NOT NULL,
    thanknotessendviaemail boolean NOT NULL,
    occasiontype integer,
    parentactivitytype integer,
    parentguid character varying(255),
    parentownertype integer,
    parentactivitypostdate timestamp without time zone,
    phoneextension character varying(255),
    phonenumber character varying(255),
    postdate timestamp with time zone NOT NULL,
    senderavatarimage character varying(255),
    senderdisplayname character varying(255),
    senderprofileimage character varying(255),
    withgooglemap boolean NOT NULL,
    layout_guid character varying(255),
    occasionwebdetail_guid character varying(255),
    owner_guid character varying(255),
    latitude bigint,
    longitude bigint,
    totalevents integer,
    publishtotwitter boolean,
    twitterdescription character varying(255),
    facebookcategory integer,
    facebookdescription character varying(255),
    facebooksubcategory integer,
    publishtofacebook boolean
);


ALTER TABLE public.bb_occasion OWNER TO blackbox;

--
-- Name: bb_occasion_attendee; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_occasion_attendee (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    attendeesource integer,
    attendingstatus integer,
    bbxuserguid character varying(255),
    bbxusername character varying(255),
    email character varying(255),
    password character varying(255),
    acknowledged boolean
);


ALTER TABLE public.bb_occasion_attendee OWNER TO blackbox;

--
-- Name: bb_occasion_bb_occasion_attendee; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_occasion_bb_occasion_attendee (
    occasions_guid character varying(255) NOT NULL,
    attendees_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_occasion_bb_occasion_attendee OWNER TO blackbox;

ALTER TABLE ONLY bb_occasion_bb_occasion_attendee
    ADD CONSTRAINT bb_occasion_bb_occasion_attendee_key UNIQUE (occasions_guid, attendees_guid);


--
-- Name: bb_occasion_layout; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_occasion_layout (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    backgroundcolor character varying(255),
    bodysize integer,
    boxcolor character varying(255),
    dataformat integer,
    font character varying(255),
    headerfont character varying(255),
    headersize integer,
    imageguid character varying(255),
    layouttype integer,
    showheading boolean NOT NULL,
    textalign character varying(255),
    textcolor character varying(255),
    videoguid character varying(255)
);


ALTER TABLE public.bb_occasion_layout OWNER TO blackbox;

--
-- Name: bb_occasion_webdetail; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_occasion_webdetail (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    cansharephoto boolean NOT NULL,
    canviewguestlist integer,
    facebookmsg character varying(255),
    promoteonfacebook boolean NOT NULL,
    promoteontwitter boolean NOT NULL,
    twittermsg character varying(255)
);


ALTER TABLE public.bb_occasion_webdetail OWNER TO blackbox;

--
-- Name: bb_occasion_webdetail_extraweblinks; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_occasion_webdetail_extraweblinks (
    bb_occasion_webdetail_guid character varying(255) NOT NULL,
    element character varying(255)
);


ALTER TABLE public.bb_occasion_webdetail_extraweblinks OWNER TO blackbox;

--
-- Name: bb_permissions; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_permissions (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    permartifacttype character varying(255),
    permkey character varying(255) NOT NULL
);


ALTER TABLE public.bb_permissions OWNER TO blackbox;

--
-- Name: bb_point; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_point (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    value bigint NOT NULL,
    user_guid character varying(255)
);


ALTER TABLE public.bb_point OWNER TO blackbox;

--
-- Name: bb_product; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_product (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    currentquantity integer NOT NULL,
    description character varying(255),
    largeimagelocation character varying(255),
    lastupdate timestamp with time zone NOT NULL,
    productamount character varying(255),
    amountlocale character varying(255),
    quantity integer NOT NULL,
    sku character varying(255) NOT NULL,
    supplierlargeimagelocation character varying(255),
    suppliertinyimagelocation character varying(255),
    tinyimagelocation character varying(255),
    supplier_guid character varying(255)
);


ALTER TABLE public.bb_product OWNER TO blackbox;

--
-- Name: bb_product_categories; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_product_categories (
    bb_product_guid character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    category_index integer NOT NULL
);


ALTER TABLE public.bb_product_categories OWNER TO blackbox;

--
-- Name: bb_profile; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_profile (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    acceptsgifts boolean NOT NULL,
    billing_address1 character varying(255),
    billing_address2 character varying(255),
    billing_city character varying(255),
    billing_country character varying(255),
    billing_county character varying(255),
    billing_state character varying(255),
    billing_zipcode character varying(255),
    birthday bytea,
    birthdayinvisible boolean NOT NULL,
    bodymods character varying(255),
    current_address1 character varying(255),
    current_address2 character varying(255),
    current_city character varying(255),
    current_country character varying(255),
    current_county character varying(255),
    current_state character varying(255),
    current_zipcode character varying(255),
    frequentcities1 character varying(255),
    frequentcities2 character varying(255),
    frequentcities3 character varying(255),
    latitude bigint,
    longitude bigint,
    address1 character varying(255),
    address2 character varying(255),
    city character varying(100),
    country character varying(100),
    county character varying(255),
    state character varying(100),
    zipcode character varying(50),
    dates boolean NOT NULL,
    donkeysex boolean NOT NULL,
    friends boolean NOT NULL,
    hookup boolean NOT NULL,
    love boolean NOT NULL,
    snuggling boolean NOT NULL,
    lookingforexplain character varying(255),
    energylevel integer NOT NULL,
    interestlevel integer NOT NULL,
    makeprivate boolean NOT NULL,
    orientation integer NOT NULL,
    polystatus integer NOT NULL,
    relationshipstatus integer NOT NULL,
    mostly character varying(255),
    phonehome character varying(255),
    phonemobile character varying(255),
    phoneother character varying(255),
    politicalviews character varying(255),
    description character varying(255),
    referfromtype integer,
    religiousviews character varying(255),
    sex integer,
    timezone character varying(255),
    visibletopersona boolean NOT NULL,
    visibletopubstream boolean NOT NULL,
    visibletosearch boolean NOT NULL,
    visibletotrialmember boolean NOT NULL,
    website character varying(255),
    user_guid character varying(255),
    current_latitude bigint,
    current_longitude bigint,
    limitedcancomment boolean,
    limitedcanfollow boolean,
    limitedcanpm boolean,
    limitedcansearch boolean,
    limitedcanseeactivity boolean,
    limitedcanseegifts boolean,
    limitedcanseepictures boolean,
    nonfriendscancomment boolean,
    nonfriendscanfollow boolean,
    nonfriendscanpm boolean,
    nonfriendscansearch boolean
);


ALTER TABLE public.bb_profile OWNER TO blackbox;

--
-- Name: bb_promo; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_promo (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    code character varying(8),
    email character varying(255),
    master_guid character varying(255) NOT NULL,
    promotype character varying(31),
    evaluationperiod integer,
    usertype integer,
    promocampaignname character varying(255),
    collectcc character varying(255),
    expirationdate timestamp with time zone
);


ALTER TABLE public.bb_promo OWNER TO blackbox;

--
-- Name: bb_promoter; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_promoter (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer
);


ALTER TABLE public.bb_promoter OWNER TO blackbox;

--
-- Name: bb_relationship; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_relationship (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    acknowledged boolean NOT NULL,
    description character varying(255),
    fromguid character varying(255) NOT NULL,
    fromownertype integer NOT NULL,
    previousweight integer,
    toguid character varying(255) NOT NULL,
    toownertype integer NOT NULL,
    weight integer NOT NULL
);


ALTER TABLE public.bb_relationship OWNER TO blackbox;

--
-- Name: bb_role; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_role (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255) NOT NULL
);


ALTER TABLE public.bb_role OWNER TO blackbox;

--
-- Name: bb_shopping_cart; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_shopping_cart (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    cartidentifier character varying(255) NOT NULL,
    totalamount character varying(255),
    totallocale character varying(255)
);


ALTER TABLE public.bb_shopping_cart OWNER TO blackbox;

--
-- Name: bb_shopping_cart_bb_cartitem; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_shopping_cart_bb_cartitem (
    bb_shopping_cart_guid character varying(255) NOT NULL,
    items_guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_shopping_cart_bb_cartitem OWNER TO blackbox;

--
-- Name: bb_social_permission; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_social_permission (
    socialpermdepth integer,
    permrelationshiptype character varying(255),
    socialpermtrustlevel double precision,
    guid character varying(255) NOT NULL
);


ALTER TABLE public.bb_social_permission OWNER TO blackbox;

--
-- Name: bb_transaction; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_transaction (
    id bigint NOT NULL,
    amount numeric(19,2),
    created timestamp with time zone NOT NULL,
    status integer,
    version integer,
    user_guid character varying(255)
);


ALTER TABLE public.bb_transaction OWNER TO blackbox;

--
-- Name: bb_user; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_user (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    name character varying(255),
    status integer,
    email character varying(255) NOT NULL,
    epxid character varying(255),
    lastonline timestamp with time zone,
    lastname character varying(255),
    maxpermanentvouches integer,
    maxwishes integer,
    password character varying(255) NOT NULL,
    type integer,
    username character varying(255) NOT NULL,
    profile_guid character varying(255),
    maxtemporaryvouches integer
);


ALTER TABLE public.bb_user OWNER TO blackbox;

ALTER TABLE ONLY bb_user
    ADD CONSTRAINT bb_user_bb_users_username_key UNIQUE (username);

--ALTER TABLE ONLY bb_user
--    ADD CONSTRAINT bb_user_bb_users_email_key UNIQUE (email);



--
-- Name: bb_user_status; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_user_status (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    profileviewcount integer NOT NULL,
    user_guid character varying(255)
);


ALTER TABLE public.bb_user_status OWNER TO blackbox;

--
-- Name: bb_viewedby; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_viewedby (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    destguid character varying(255),
    viewedbytype integer,
    user_guid character varying(255)
);


ALTER TABLE public.bb_viewedby OWNER TO blackbox;

--
-- Name: bb_vouch; Type: TABLE; Schema: public; Owner: blackbox; Tablespace: 
--

CREATE TABLE bb_vouch (
    guid character varying(255) NOT NULL,
    created timestamp with time zone NOT NULL,
    modified timestamp with time zone NOT NULL,
    version bigint,
    expirationdate timestamp with time zone NOT NULL,
    targetguid character varying(255),
    targetownertype integer,
    voucherguid character varying(255),
    voucherownertype integer,
    accepted boolean,
    count integer,
    description character varying(255)
);


ALTER TABLE public.bb_vouch OWNER TO blackbox;
