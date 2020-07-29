package android.panos.ram.rammilkcollection.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MilkCollection.db";
    public SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "create table config " +
                        "(id integer primary key,clientid text,vendorwerks text,webserviceurl text,dsr0code text,dsr1code text,checkdsrcode text,diakinisidsrcode text," +
                        "checkmetaggisidsrcode text,metaggisidsrcode text, dsr0num text,dsr1num text,checkdsrnum text,diakinisidsrnum text,checkmetaggisidsrnum text,metaggisidsrnum text," +
                        "last_sup_rv text,last_meterial_rv text,last_docseries_rv text,last_suptosup_rv text," +
                        "last_suptosuplines_rv text,last_suptosuplines2_rv text,last_suptobarcode_rv text,last_salesman_rv text,last_shipvia_rv text,last_transportation_rv text,last_retailcustomer_rv," +
                        "routeid text,salsmid text,transpid text,printer text)"
        );

        db.execSQL(
                "create table company " +
                        "(id integer primary key,name text,address text,city text,phone1 text,phone2 text,fax text,zipcode text,argemi text,afm text,doy text)"
        );

        db.execSQL(
                "create table docseries " +
                        "(id integer primary key,domaintype text,codeid text,docseries text,dockind text,docseriesdescr text,braid text,ord text,mode text," +
                        "qmode text,nextdocnum text,cancelseries text,defaultcustomercode text,paymentdsrid text,interfacetype text,printername text," +
                        "formfilename text,printer_encoding text,pricefld text,flags text,copies text,surid text,numgroup text,pkid text,ptrid text,rotid text," +
                        "isvalid text,rv text,policies text,copycodeid text,trsid text,activeretail text,stoid text,shcid text,shvid text," +
                        "domaintypeinsert text,clientgroup text,taxescode text)"
        );


        db.execSQL(
                "create table supplier " +
                        "(id integer primary key,supid text,code text,name text,occupation text,street1 text,city1 text,zipcode1 text,phone11 text," +
                        "identitynum text,afm text,doyid text,doy text,defaultdiscount text,shvid text,npgid text,ocpid text,grpid text," +
                        "fpastatus text,fldid1 text,cntid text,pos text,isvalid text,rv text,z_pagolekaniid text,z_seira text,vendorwerks text,remarks text," +
                        "prtid text,carid text,z_pagolekaniid2 text,zpercentage text)"
        );

        db.execSQL(
                "create table milksuptobarcode " +
                        "(id integer primary key,zstsid text,zpagolekaniid2 text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table milksuptosup " +
                        "(id integer primary key,zstsid text,comid text,supid text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table milksuptosuplines " +
                        "(id integer primary key,zstslinesid text,comid text,zstsid text,supid text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table milksuptosuplines2 " +
                        "(id integer primary key,zstslines2id text,comid text,zstsid text,iteid text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table retailcustomer " +
                        "(id integer primary key,rcustid text,code text,name text,occupation text,street1 text,city1 text,zipcode1 text,phone11 text,phone12 text,fax1 text," +
                        "telex1 text,district1 text,street2 text,city2 text,zipcode2 text,phone21 text,phone22 text,fax2 text,telex2 text,district2 text," +
                        "identitynum text,afm text,doyid text,doy text,difaultdiscount text,shvid text,npgid text,prcid text,ocpid text,grpid text," +
                        "fpastatus text,fldid1 text,cntid text,carid text,rotid text,delivman text,ptrid text,colidsalesman text,manager text,acccrdlimit text," +
                        "pos text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table retailmaterial " +
                        "(id integer primary key,iteid text,code text,descr text,descr2 text,mainszlid text,price text,fprice text,whsprice text,fwhsprice text," +
                        "vtcid text,mu text,su text,mu2 text,mu2_1 text,mu1_2mode text,ictid text,igpid text,igsid text,igtid text,mpcid text," +
                        "recomqty text,isoffer text,glmid text,efkax text,isvalid text,rv text)"
        );


        db.execSQL(
                "create table trades " +
                        "(id integer primary key,collid text,trchid text,cid text,domaintype text,xrisi text,docseries text,docnum text,tradecode text,hmer text,time text,fromkeb text,tokeb text,stoid text,secstoid text,braid,custid text,custcode text,secjustificationfrom text,daafrom text,daato text," +
                        "supid text,shvid text,trsid text,salesmanid text,zpagolekaniid2 text,dromologionum text,zfat,ztemperature,zph,notes text,fromtradecode text,uptotradecode text,iscancel text,isdiakinisi text,ismetaggisi text,ismetaggisicheck text,iscanceled text,ischecked text,updated text,chkid text,xlm text)"
        );


        db.execSQL(
                "create table tradelines " +
                        "(id integer primary key,trid text,aa text,iteid text,qty text,z_autonum text,z_wpid text,fromzwpid text)"
        );


        db.execSQL(
                "create table route " +
                        "(id integer primary key,codeid text,descr text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table salesman " +
                        "(id integer primary key,salsmid text,code text,name text,braid text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table shipvia " +
                        "(id integer primary key,codeid text,descr text,isvalid text,rv text)"
        );



        db.execSQL(
                "create table store " +
                        "(id integer primary key,codeid text,descr text,name text,braid tect,isvalid text,rv text)"
        );

        db.execSQL(
                "create table transportation " +
                        "(id integer primary key,codeid text,code text,descr text,isvalid text,rv text)"
        );

        db.execSQL(
                "create table collection " +
                        "(id integer,routeid text,stationid text,pagolekani text," +
                        "barcode text,fat text,temperature text,ph text,amount text)"
        );

        db.execSQL(
                "create table collectionlines " +
                        "(aa text,supid text,iteid text,remarks text,amount text)"
        );

        db.execSQL(
                "create table collectionchamberlines " +
                        "(chamberid text,amount text)"
        );


        db.execSQL(
                "create table transfersourcetrade " +
                        "(cid text,domaintype text,xrisi text,docseries text,docnum text,tradecode text,hmer text," +
                        "supid text,shvid text,trsid text,salesmanid text,dromologionum text,notes text,fromtradecode text,uptotradecode text)"
        );


        db.execSQL(
                "create table destinationtransferdata " +
                        "(destinationchamberid text,initialqty text,transqty text,sample text)"
        );

        db.execSQL(
                "create table transferchamberlines " +
                        "(aa text,sourcechamberid text,iteid text,sourceqty text,sourcesample text)"
        );

        db.execSQL(
                "create table sourcetransferchamberdata " +
                        "(sourcechamberid text,sourcesample text,transqty text,destinationchamp1qty text,destinationchamp2qty text,destinationchamp3qty text,destinationchamp4qty text," +
                        "destinationchamp5qty text,destinationchamp6qty text,destinationchamp7qty text,destinationchamp8qty text," +
                        "destinationchamp9qty text,destinationchamp10qty text,destinationchamp11qty text,destinationchamp12qty text," +
                        " destinationchamp13qty text,destinationchamp14qty text,destinationchamp15qty text)"
        );

        db.execSQL(
                "create table allcollections " +
                        "(id integer primary key,hmer text,aa text,routeid text,stationid text,pagolekani text," +
                        "barcode text,fat text,temperature text,ph text,amount text,iscanceled text)"
        );

        db.execSQL(
                "create table tradechecks " +
                        "(id integer primary key,hmer text,aa text,fromtradecode text,uptotradecode text,iscanceled text)"
        );

        db.execSQL(
                "create table factories " +
                        "(id integer primary key,codeid text,code text,descr text,address text,branch text)"
        );

        ContentValues contentValues = new ContentValues();
        contentValues.put("codeid", "1801");
        contentValues.put("code", "18010001");
        contentValues.put("descr", "ΤΡΙΚΑΛΑ ΕΡΓΟΣΑΣΙΟ");
        contentValues.put("address", "5ΧΛΜ ΤΡΙΚΑΛΩΝ ΠΥΛΗΣ ΤΡΙΚΑΛΑ");
        contentValues.put("branch", "ΚΕΝΤΡΙΚΟ");

        db.insert("factories", null, contentValues);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("codeid", "1802");
        contentValues1.put("code", "18020001");
        contentValues1.put("descr", "ΛΑΡΙΣΑ ΕΡΓΟΣTΑΣΙΟ");
        contentValues1.put("address", "16ΧΛΜ ΛΑΡΙΣΑ ΘΕΣ/ΚΗΣ ΛΑΡΙΣΑ");
        contentValues1.put("branch", "ΥΠΟΚ/ΜΑ ΛΑΡΙΣΑΣ");

        db.insert("factories", null, contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("codeid", "1803");
        contentValues2.put("code", "18030001");
        contentValues2.put("descr", "ΞΑΝΘΗ ΕΡΓΟΣΑΣΙΟ");
        contentValues2.put("address", "3ΧΛΜ ΞΑΝΘΗ ΛΑΓΟΥΣ ΞΑΝΘΗ");
        contentValues1.put("branch", "ΥΠΟΚ/ΜΑ ΞΑΝΘΗΣ");

        db.insert("factories", null, contentValues2);

        db.execSQL(
                "create table diakinisi " +
                        "(id integer primary key,customer text,stoid text,secstoid text,descr text)"
        );


        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-101");
        contentValues.put("stoid", "1801");
        contentValues.put("secstoid", "1802");
        contentValues.put("descr", "ΤΥΡΑΣ ΟΛΥΜΠΟΣ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-560");
        contentValues.put("stoid", "1801");
        contentValues.put("secstoid", "1803");
        contentValues.put("descr", "ΤΥΡΑΣ ΡΟΔΟΠΗ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-099");
        contentValues.put("stoid", "1802");
        contentValues.put("secstoid", "1801");
        contentValues.put("descr", "ΟΥΜΠΟΣ ΤΥΡΑΣ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-560");
        contentValues.put("stoid", "1802");
        contentValues.put("secstoid", "1803");
        contentValues.put("descr", "ΟΥΜΠΟΣ ΡΟΔΟΠΗ");

        db.insert("diakinisi", null, contentValues);


        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-099");
        contentValues.put("stoid", "1803");
        contentValues.put("secstoid", "1801");
        contentValues.put("descr", "ΡΟΔΟΠΗ ΤΥΡΑΣ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-101");
        contentValues.put("stoid", "1803");
        contentValues.put("secstoid", "1802");
        contentValues.put("descr", "ΡΟΔΟΠΗ ΟΛΥΜΠΟΣ");

        db.insert("diakinisi", null, contentValues);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS config");
        db.execSQL("DROP TABLE IF EXISTS company");
        db.execSQL("DROP TABLE IF EXISTS docseries");
        db.execSQL("DROP TABLE IF EXISTS supplier");
        db.execSQL("DROP TABLE IF EXISTS milksuptobarcode");
        db.execSQL("DROP TABLE IF EXISTS milksuptosup");
        db.execSQL("DROP TABLE IF EXISTS milksuptosuplines");
        db.execSQL("DROP TABLE IF EXISTS milksuptosuplines2");
        db.execSQL("DROP TABLE IF EXISTS retailcustomer");
        db.execSQL("DROP TABLE IF EXISTS retailmaterial");
        db.execSQL("DROP TABLE IF EXISTS trades");
        db.execSQL("DROP TABLE IF EXISTS tradelines");
        db.execSQL("DROP TABLE IF EXISTS route");
        db.execSQL("DROP TABLE IF EXISTS salesman");
        db.execSQL("DROP TABLE IF EXISTS shipvia");
        db.execSQL("DROP TABLE IF EXISTS store");
        db.execSQL("DROP TABLE IF EXISTS transportation");
        db.execSQL("DROP TABLE IF EXISTS collection");
        db.execSQL("DROP TABLE IF EXISTS collectionlines");
        db.execSQL("DROP TABLE IF EXISTS collectionchamberlines");
        db.execSQL("DROP TABLE IF EXISTS allcollections");
        db.execSQL("DROP TABLE IF EXISTS tradechecks");
        db.execSQL("DROP TABLE IF EXISTS factories");
        db.execSQL("DROP TABLE IF EXISTS diakinisi");
        db.execSQL("DROP TABLE IF EXISTS destinationtransferdata");
        db.execSQL("DROP TABLE IF EXISTS transferchamberlines");
        db.execSQL("DROP TABLE IF EXISTS sourcetransferchamberdata");
        db.execSQL("DROP TABLE IF EXISTS transfersourcetrade");

        onCreate(db);
    }

        public boolean createFactoriesTable() {

            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("DROP TABLE IF EXISTS factories");

            db.execSQL(
                    "create table factories " +
                            "(id integer primary key,codeid text,code text,descr text,address text,branch text)"
            );

            ContentValues contentValues = new ContentValues();
            contentValues.put("codeid", "1801");
            contentValues.put("code", "18010001");
            contentValues.put("descr", "ΤΡΙΚΑΛΑ ΕΡΓΟΣΑΣΙΟ");
            contentValues.put("address", "5ΧΛΜ ΤΡΙΚΑΛΩΝ ΠΥΛΗΣ ΤΡΙΚΑΛΑ");
            contentValues.put("branch", "ΚΕΝΤΡΙΚΟ");

            db.insert("factories", null, contentValues);

            contentValues = new ContentValues();
            contentValues.put("codeid", "1802");
            contentValues.put("code", "18020001");
            contentValues.put("descr", "ΛΑΡΙΣΑ ΕΡΓΟΣTΑΣΙΟ");
            contentValues.put("address", "16ΧΛΜ ΛΑΡΙΣΑ ΘΕΣ/ΚΗΣ ΛΑΡΙΣΑ");
            contentValues.put("branch", "ΥΠΟΚ/ΜΑ ΛΑΡΙΣΑΣ");

            db.insert("factories", null, contentValues);

            contentValues = new ContentValues();
            contentValues.put("codeid", "1803");
            contentValues.put("code", "18030001");
            contentValues.put("descr", "ΞΑΝΘΗ ΕΡΓΟΣΑΣΙΟ");
            contentValues.put("branch", "ΥΠΟΚ/ΜΑ ΞΑΝΘΗΣ");

            db.insert("factories", null, contentValues);


            return true;

        }

    public boolean createTransferTables() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS destinationtransferdata");
        db.execSQL("DROP TABLE IF EXISTS transferchamberlines");
        db.execSQL("DROP TABLE IF EXISTS sourcetransferchamberdata");
        db.execSQL("DROP TABLE IF EXISTS transfersourcetrade");


        db.execSQL(
                "create table destinationtransferdata " +
                        "(destinationchamberid text,initialqty text,transqty text,sample text)"
        );

        db.execSQL(
                "create table transferchamberlines " +
                        "(aa text,sourcechamberid text,iteid text,sourceqty text,sourcesample text)"
        );


        db.execSQL(
                "create table sourcetransferchamberdata " +
                        "(sourcechamberid text,sourcesample text,transqty text,destinationchamp1qty text,destinationchamp2qty text,destinationchamp3qty text,destinationchamp4qty text," +
                        "destinationchamp5qty text,destinationchamp6qty text,destinationchamp7qty text,destinationchamp8qty text," +
                        "destinationchamp9qty text,destinationchamp10qty text,destinationchamp11qty text,destinationchamp12qty text," +
                        " destinationchamp13qty text,destinationchamp14qty text,destinationchamp15qty text)"
        );

        db.execSQL(
                "create table transfersourcetrade " +
                        "(cid text,domaintype text,xrisi text,docseries text,docnum text,tradecode text,hmer text," +
                        "supid text,shvid text,trsid text,salesmanid text,dromologionum text,notes text,fromtradecode text,uptotradecode text)"
        );


        return true;

    }


    public boolean createCompanyTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS company");

        db.execSQL(
                "create table company " +
                        "(id integer primary key,name text,address text,city text,phone1 text,phone2 text,fax text,zipcode text,argemi text,afm text,doy text)"
        );

        return true;

    }

    public boolean createDiakinisiTable() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS diakinisi");

        db.execSQL(
                "create table diakinisi " +
                        "(id integer primary key,customer text,stoid text,secstoid text,descr text)"
        );


        ContentValues contentValues = new ContentValues();

        contentValues.put("customer", "099-099-101");
        contentValues.put("stoid", "1801");
        contentValues.put("secstoid", "1802");
        contentValues.put("descr", "ΤΥΡΑΣ ΟΛΥΜΠΟΣ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-560");
        contentValues.put("stoid", "1801");
        contentValues.put("secstoid", "1803");
        contentValues.put("descr", "ΤΥΡΑΣ ΡΟΔΟΠΗ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-099");
        contentValues.put("stoid", "1802");
        contentValues.put("secstoid", "1801");
        contentValues.put("descr", "ΟΥΜΠΟΣ ΤΥΡΑΣ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-560");
        contentValues.put("stoid", "1802");
        contentValues.put("secstoid", "1803");
        contentValues.put("descr", "ΟΥΜΠΟΣ ΡΟΔΟΠΗ");

        db.insert("diakinisi", null, contentValues);


        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-099");
        contentValues.put("stoid", "1803");
        contentValues.put("secstoid", "1801");
        contentValues.put("descr", "ΡΟΔΟΠΗ ΤΥΡΑΣ");

        db.insert("diakinisi", null, contentValues);

        contentValues = new ContentValues();

        contentValues.put("customer", "099-099-101");
        contentValues.put("stoid", "1803");
        contentValues.put("secstoid", "1802");
        contentValues.put("descr", "ΡΟΔΟΠΗ ΟΛΥΜΠΟΣ");

        db.insert("diakinisi", null, contentValues);

        return true;

    }


    public boolean createNewTables() {

        SQLiteDatabase db = this.getWritableDatabase();


        //db.execSQL("DROP TABLE IF EXISTS config");
        db.execSQL("DROP TABLE IF EXISTS collection");
        db.execSQL("DROP TABLE IF EXISTS collectionlines");
        db.execSQL("DROP TABLE IF EXISTS collectionchamberlines");
        db.execSQL("DROP TABLE IF EXISTS allcollections");

        db.execSQL("DROP TABLE IF EXISTS trades");

        db.execSQL("DROP TABLE IF EXISTS tradelines");

        db.execSQL("DROP TABLE IF EXISTS tradechecks");




/*
        db.execSQL(
                "create table config " +
                        "(id integer primary key,clientid text,vendorwerks text,webserviceurl text,dsr0code text,dsr1code text,checkdsrcode text,diakinisidsrcode text," +
                        "checkmetaggisidsrcode text,metaggisidsrcode text, dsr0num text,dsr1num text,checkdsrnum text,diakinisidsrnum text,checkmetaggisidsrnum text,metaggisidsrnum text," +
                        "last_sup_rv text,last_meterial_rv text,last_docseries_rv text,last_suptosup_rv text," +
                        "last_suptosuplines_rv text,last_suptosuplines2_rv text,last_suptobarcode_rv text,last_salesman_rv text,last_shipvia_rv text,last_transportation_rv text,last_retailcustomer_rv," +
                        "routeid text,salsmid text,transpid text,printer text)"
        );
*/



        db.execSQL(
                "create table collection " +
                        "(id integer,routeid text,stationid text,pagolekani text," +
                        "barcode text,fat text,temperature text,ph text,amount text)"
        );

        db.execSQL(
                "create table collectionlines " +
                        "(aa text,supid text,iteid text,remarks text,amount text)"
        );

        db.execSQL(
                "create table collectionchamberlines " +
                        "(chamberid text,amount text)"
        );

        db.execSQL(
                "create table allcollections " +
                        "(id integer primary key,hmer text,aa text,routeid text,stationid text,pagolekani text," +
                        "barcode text,fat text,temperature text,ph text,amount text,iscanceled text)"
        );



        db.execSQL(
                "create table trades " +
                        "(id integer primary key,collid text,trchid text,cid text,domaintype text,xrisi text,docseries text,docnum text,tradecode text,hmer text,time text,fromkeb text,tokeb text,stoid text,secstoid text,braid,custid text,custcode text,secjustificationfrom text,daafrom text,daato text," +
                        "supid text,shvid text,trsid text,salesmanid text,zpagolekaniid2 text,dromologionum text,zfat,ztemperature,zph,notes text,fromtradecode text,uptotradecode text,iscancel text,isdiakinisi text,ismetaggisi text,ismetaggisicheck text,iscanceled text,ischecked text,updated text,chkid text)"
        );


        db.execSQL(
                "create table tradelines " +
                        "(id integer primary key,trid text,aa text,iteid text,qty text,z_autonum text,z_wpid text,fromzwpid text)"
        );


        db.execSQL(
                "create table tradechecks " +
                        "(id integer primary key,hmer text,aa text,fromtradecode text,uptotradecode text,iscanceled text)"
        );

        return true;
    }

    public boolean updateSuppliers (String supid,String code,String name,String occupation ,String street1 ,String city1 ,String phone11 ,
                                    String identitynum ,String afm ,String doyid ,String fpastatus,String fldid1,
                                    String isvalid  ,String rv ,String z_seira ,String vendorwerks ,String remarks,String shvid,String z_pagolekaniid2)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("occupation", occupation);
        contentValues.put("street1", street1);
        contentValues.put("city1", city1);
        contentValues.put("phone11", phone11);
        contentValues.put("identitynum", identitynum);
        contentValues.put("afm", afm);
        contentValues.put("doyid", doyid);
        contentValues.put("fpastatus", fpastatus);
        contentValues.put("fldid1", fldid1);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);
        contentValues.put("z_seira", z_seira);
        contentValues.put("vendorwerks", vendorwerks);
        contentValues.put("remarks", remarks);
        contentValues.put("shvid", shvid);
        contentValues.put("z_pagolekaniid2", z_pagolekaniid2);

        db.update("supplier", contentValues, "supid = ? ", new String[] {supid} );
        return true;
    }

    public boolean insertSuppliers (String supid,String code,String name,String occupation ,String street1 ,String city1 ,String phone11 ,
                                       String identitynum ,String afm ,String doyid ,String fpastatus,String fldid1,
                                       String isvalid  ,String rv ,String z_seira ,String vendorwerks ,String remarks,String shvid,String z_pagolekaniid2)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("supid", supid);
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("occupation", occupation);
        contentValues.put("street1", street1);
        contentValues.put("city1", city1);
        contentValues.put("phone11", phone11);
        contentValues.put("identitynum", identitynum);
        contentValues.put("afm", afm);
        contentValues.put("doyid", doyid);
        contentValues.put("fpastatus", fpastatus);
        contentValues.put("fldid1", fldid1);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);
        contentValues.put("z_seira", z_seira);
        contentValues.put("vendorwerks", vendorwerks);
        contentValues.put("remarks", remarks);
        contentValues.put("shvid", shvid);
        contentValues.put("z_pagolekaniid2", z_pagolekaniid2);

        db.insert("supplier", null, contentValues);
        return true;
    }

    public boolean updateCustomers (String rcustid,String code,String name,String occupation,String street1,String city1,String zipcode1,String phone11,String phone12,String fax1,String telex1,
                                    String district1,String street2,String city2,String zipcode2,String phone21,String phone22,String fax2,String telex2,String district2, String identitynum,
                                    String afm, String doyid,String doy,String difaultdiscount,String shvid,String npgid,String prcid,String ocpid,String grpid,String fpastatus,String fldid1,String cntid,String carid,String rotid,String delivman,
                                    String ptrid,String colidsalesman,String manager,String acccrdlimit,String pos,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("occupation", occupation);
        contentValues.put("street1", street1);
        contentValues.put("city1", city1);
        contentValues.put("zipcode1", zipcode1);
        contentValues.put("phone11", phone11);
        contentValues.put("phone12", phone12);
        contentValues.put("fax1", fax1);
        contentValues.put("telex1", telex1);
        contentValues.put("district1", district1);
        contentValues.put("street2", street2);
        contentValues.put("city2", city2);
        contentValues.put("zipcode2", zipcode2);
        contentValues.put("phone21", phone21);
        contentValues.put("phone22", phone22);
        contentValues.put("fax2", fax2);
        contentValues.put("telex2", telex2);
        contentValues.put("district2", district2);
        contentValues.put("identitynum", identitynum);
        contentValues.put("afm", afm);
        contentValues.put("doyid", doyid);
        contentValues.put("doy", doy);
        contentValues.put("difaultdiscount", difaultdiscount);
        contentValues.put("shvid", shvid);
        contentValues.put("npgid", npgid);
        contentValues.put("prcid", prcid);
        contentValues.put("ocpid", ocpid);
        contentValues.put("grpid", grpid);
        contentValues.put("fpastatus", fpastatus);
        contentValues.put("fldid1", fldid1);
        contentValues.put("cntid", cntid);
        contentValues.put("carid", carid);
        contentValues.put("rotid", rotid);
        contentValues.put("delivman", delivman);
        contentValues.put("ptrid", ptrid);
        contentValues.put("colidsalesman", colidsalesman);
        contentValues.put("manager", manager);
        contentValues.put("acccrdlimit", acccrdlimit);
        contentValues.put("pos", pos);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);


        db.update("retailcustomer", contentValues, "rcustid = ? ", new String[] {rcustid} );
        return true;
    }

    public boolean insertCustomers (String rcustid,String code,String name,String occupation,String street1,String city1,String zipcode1,String phone11,String phone12,String fax1,String telex1,
                                    String district1,String street2,String city2,String zipcode2,String phone21,String phone22,String fax2,String telex2,String district2, String identitynum,
                                    String afm, String doyid,String doy,String difaultdiscount,String shvid,String npgid,String prcid,String ocpid,String grpid,String fpastatus,String fldid1,String cntid,String carid,String rotid,String delivman,
                                    String ptrid,String colidsalesman,String manager,String acccrdlimit,String pos,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("rcustid", rcustid);
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("occupation", occupation);
        contentValues.put("street1", street1);
        contentValues.put("city1", city1);
        contentValues.put("zipcode1", zipcode1);
        contentValues.put("phone11", phone11);
        contentValues.put("phone12", phone12);
        contentValues.put("fax1", fax1);
        contentValues.put("telex1", telex1);
        contentValues.put("district1", district1);
        contentValues.put("street2", street2);
        contentValues.put("city2", city2);
        contentValues.put("zipcode2", zipcode2);
        contentValues.put("phone21", phone21);
        contentValues.put("phone22", phone22);
        contentValues.put("fax2", fax2);
        contentValues.put("telex2", telex2);
        contentValues.put("district2", district2);
        contentValues.put("identitynum", identitynum);
        contentValues.put("afm", afm);
        contentValues.put("doyid", doyid);
        contentValues.put("doy", doy);
        contentValues.put("difaultdiscount", difaultdiscount);
        contentValues.put("shvid", shvid);
        contentValues.put("npgid", npgid);
        contentValues.put("prcid", prcid);
        contentValues.put("ocpid", ocpid);
        contentValues.put("grpid", grpid);
        contentValues.put("fpastatus", fpastatus);
        contentValues.put("fldid1", fldid1);
        contentValues.put("cntid", cntid);
        contentValues.put("carid", carid);
        contentValues.put("rotid", rotid);
        contentValues.put("delivman", delivman);
        contentValues.put("ptrid", ptrid);
        contentValues.put("colidsalesman", colidsalesman);
        contentValues.put("manager", manager);
        contentValues.put("acccrdlimit", acccrdlimit);
        contentValues.put("pos", pos);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("retailcustomer", null, contentValues);

        return true;
    }

    public boolean updateTransportation(String codeid,String code,String descr,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("descr", descr);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("transportation", contentValues, "codeid = ? ", new String[] {codeid} );
        return true;
    }

    public boolean insertTransportation(String codeid,String code,String descr,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("codeid", codeid);
        contentValues.put("code", code);
        contentValues.put("descr", descr);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("transportation", null, contentValues);
        return true;
    }

    public boolean updateShipvia(String codeid,String descr,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("descr", descr);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("shipvia", contentValues, "codeid = ? ", new String[] {codeid} );
        return true;
    }

    public boolean insertShipvia (String codeid,String descr,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("codeid", codeid);
        contentValues.put("descr", descr);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("shipvia", null, contentValues);
        return true;
    }

    public boolean updateSalesman(String salsmid,String code,String name,String braid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("braid", braid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("salesman", contentValues, "salsmid = ? ", new String[] {salsmid} );
        return true;
    }

    public boolean insertSalesman (String salsmid,String code,String name,String braid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salsmid", salsmid);
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("braid", braid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("salesman", null, contentValues);
        return true;
    }


    public boolean updateSupToSupBarcode (String zpagolekaniid2,String zstsid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zpagolekaniid2", zpagolekaniid2);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("milksuptobarcode", contentValues, "zstsid = ? ", new String[] {zstsid} );
        return true;
    }

    public boolean insertSupToSupBarcode (String zpagolekaniid2,String zstsid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zpagolekaniid2", zpagolekaniid2);
        contentValues.put("zstsid", zstsid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("milksuptobarcode", null, contentValues);
        return true;
    }

    public boolean updateSupToSupLines2 (String zstslines2id,String zstsid,String comid,String iteid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zstsid", zstsid);
        contentValues.put("iteid", iteid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("milksuptosuplines2", contentValues, "zstslines2id = ? and comid = ? ", new String[] {zstslines2id,comid} );
        return true;
    }

    public boolean insertSupToSupLines2 (String zstslines2id,String zstsid,String comid,String iteid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zstslines2id", zstslines2id);
        contentValues.put("comid", comid);
        contentValues.put("zstsid", zstsid);
        contentValues.put("comid", comid);
        contentValues.put("iteid", iteid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("milksuptosuplines2", null, contentValues);
        return true;
    }

    public boolean updateSupToSupLines (String zstslinesid,String zstsid,String comid,String supid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zstsid", zstsid);
        contentValues.put("supid", supid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("milksuptosuplines", contentValues, "zstslinesid = ? and comid = ? ", new String[] {zstslinesid,comid} );
        return true;
    }

    public boolean insertSupToSupLines (String zstslinesid,String zstsid,String comid,String supid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zstslinesid", zstslinesid);
        contentValues.put("comid", comid);
        contentValues.put("zstsid", zstsid);
        contentValues.put("comid", comid);
        contentValues.put("supid", supid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("milksuptosuplines", null, contentValues);
        return true;
    }

    public boolean updateSupToSup (String zstsid,String comid,String supid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("supid", supid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("milksuptosup", contentValues, "zstsid = ? and comid = ? ", new String[] {zstsid,comid} );
        return true;
    }

    public boolean insertSupToSup (String zstsid,String comid,String supid,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("zstsid", zstsid);
        contentValues.put("comid", comid);
        contentValues.put("supid", supid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("milksuptosup", null, contentValues);
        return true;
    }

        public boolean updateMaterials (String iteid,String code,String descr,String descr2 ,String price ,String fprice ,String whsprice ,
                                    String fwhsprice ,String vtcid ,String mu ,String su,String mu2_1,String mu1_2mode  ,String ictid ,
                                    String igpid ,String igsid ,String igtid,String mpcid,String mainszlid,String recomqty ,String isoffer,String glmid,
                                    String efkax,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("descr", descr);
        contentValues.put("descr2", descr2);
        contentValues.put("price", price);
        contentValues.put("fprice", fprice);
        contentValues.put("whsprice", whsprice);
        contentValues.put("fwhsprice", fwhsprice);
        contentValues.put("vtcid", vtcid);
        contentValues.put("mu", mu);
        contentValues.put("su", su);
        contentValues.put("mu2_1", mu2_1);
        contentValues.put("mu1_2mode", mu1_2mode);
        contentValues.put("ictid", ictid);
        contentValues.put("igpid", igpid);
        contentValues.put("igsid", igsid);
        contentValues.put("igtid", igtid);
        contentValues.put("mpcid", mpcid);
        contentValues.put("mainszlid", mainszlid);
        contentValues.put("recomqty", recomqty);
        contentValues.put("isoffer", isoffer);
        contentValues.put("glmid", glmid);
        contentValues.put("efkax", efkax);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.update("retailmaterial", contentValues, "iteid = ? ", new String[] {iteid} );
        return true;
    }

    public boolean insertMaterials (String iteid,String code,String descr,String descr2 ,String price ,String fprice ,String whsprice ,
                                    String fwhsprice ,String vtcid ,String mu ,String su,String mu2_1,String mu1_2mode  ,String ictid ,
                                    String igpid ,String igsid ,String igtid,String mpcid,String mainszlid,String recomqty ,String isoffer,String glmid,
                                    String efkax,String isvalid,String rv)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("iteid", iteid);
        contentValues.put("code", code);
        contentValues.put("descr", descr);
        contentValues.put("descr2", descr2);
        contentValues.put("price", price);
        contentValues.put("fprice", fprice);
        contentValues.put("whsprice", whsprice);
        contentValues.put("fwhsprice", fwhsprice);
        contentValues.put("vtcid", vtcid);
        contentValues.put("mu", mu);
        contentValues.put("su", su);
        contentValues.put("mu2_1", mu2_1);
        contentValues.put("mu1_2mode", mu1_2mode);
        contentValues.put("ictid", ictid);
        contentValues.put("igpid", igpid);
        contentValues.put("igsid", igsid);
        contentValues.put("igtid", igtid);
        contentValues.put("mpcid", mpcid);
        contentValues.put("mainszlid", mainszlid);
        contentValues.put("recomqty", recomqty);
        contentValues.put("isoffer", isoffer);
        contentValues.put("glmid", glmid);
        contentValues.put("efkax", efkax);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);

        db.insert("retailmaterial", null, contentValues);
        return true;
    }

    public boolean updateDocseries (String domaintype,String codeid,String docseries,String dockind,String docseriesdescr,String braid,String ord,String mode,String qmode,String nextdocnum,String cancelseries,
                                    String defaultcustomercode,String paymentdsrid,String interfacetype,String printername,String formfilename,String printer_encoding,String pricefld,String flags,String copies,String surid,
                                    String numgroup,String pkid,String ptrid,String rotid,String isvalid,String rv,String policies,String copycodeid,String trsid,String activeretail,String stoid,String shcid,String shvid,String domaintypeinsert,String clientgroup,String taxescode)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docseries", docseries);
        contentValues.put("dockind", dockind);
        contentValues.put("docseriesdescr", docseriesdescr);
        contentValues.put("braid", braid);
        contentValues.put("ord", ord);
        contentValues.put("mode", mode);
        contentValues.put("qmode", qmode);
        contentValues.put("nextdocnum", nextdocnum);
        contentValues.put("cancelseries", cancelseries);
        contentValues.put("defaultcustomercode", defaultcustomercode);
        contentValues.put("paymentdsrid", paymentdsrid);
        contentValues.put("interfacetype", interfacetype);
        contentValues.put("printername", printername);
        contentValues.put("formfilename", formfilename);
        contentValues.put("printer_encoding", printer_encoding);
        contentValues.put("pricefld", pricefld);
        contentValues.put("flags", flags);
        contentValues.put("copies", copies);
        contentValues.put("surid", surid);
        contentValues.put("numgroup", numgroup);
        contentValues.put("pkid", pkid);
        contentValues.put("ptrid", ptrid);
        contentValues.put("rotid", rotid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);
        contentValues.put("policies", policies);
        contentValues.put("copycodeid", copycodeid);
        contentValues.put("trsid", trsid);
        contentValues.put("activeretail", activeretail);
        contentValues.put("stoid", stoid);
        contentValues.put("shcid", shcid);
        contentValues.put("shvid", shvid);
        contentValues.put("domaintypeinsert", domaintypeinsert);
        contentValues.put("clientgroup", clientgroup);
        contentValues.put("taxescode", taxescode);

        db.update("docseries", contentValues, "domaintype = ? and codeid = ? ", new String[] {domaintype,codeid} );

        return true;
    }


    public boolean insertDocseries (String domaintype,String codeid,String docseries,String dockind,String docseriesdescr,String braid,String ord,String mode,String qmode,String nextdocnum,String cancelseries,
                                    String defaultcustomercode,String paymentdsrid,String interfacetype,String printername,String formfilename,String printer_encoding,String pricefld,String flags,String copies,String surid,
                                    String numgroup,String pkid,String ptrid,String rotid,String isvalid,String rv,String policies,String copycodeid,String trsid,String activeretail,String stoid,String shcid,String shvid,String domaintypeinsert,String clientgroup,String taxescode)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("domaintype", domaintype);
        contentValues.put("codeid", codeid);
        contentValues.put("docseries", docseries);
        contentValues.put("dockind", dockind);
        contentValues.put("docseriesdescr", docseriesdescr);
        contentValues.put("braid", braid);
        contentValues.put("ord", ord);
        contentValues.put("mode", mode);
        contentValues.put("qmode", qmode);
        contentValues.put("nextdocnum", nextdocnum);
        contentValues.put("cancelseries", cancelseries);
        contentValues.put("defaultcustomercode", defaultcustomercode);
        contentValues.put("paymentdsrid", paymentdsrid);
        contentValues.put("interfacetype", interfacetype);
        contentValues.put("printername", printername);
        contentValues.put("formfilename", formfilename);
        contentValues.put("printer_encoding", printer_encoding);
        contentValues.put("pricefld", pricefld);
        contentValues.put("flags", flags);
        contentValues.put("copies", copies);
        contentValues.put("surid", surid);
        contentValues.put("numgroup", numgroup);
        contentValues.put("pkid", pkid);
        contentValues.put("ptrid", ptrid);
        contentValues.put("rotid", rotid);
        contentValues.put("isvalid", isvalid);
        contentValues.put("rv", rv);
        contentValues.put("policies", policies);
        contentValues.put("copycodeid", copycodeid);
        contentValues.put("trsid", trsid);
        contentValues.put("activeretail", activeretail);
        contentValues.put("stoid", stoid);
        contentValues.put("shcid", shcid);
        contentValues.put("shvid", shvid);
        contentValues.put("domaintypeinsert", domaintypeinsert);
        contentValues.put("clientgroup", clientgroup);
        contentValues.put("taxescode", taxescode);

        db.insert("docseries", null, contentValues);
        return true;
    }




    public boolean insertConfig (String clientid,String vendorwerks, String webserviceurl,String dsr0code,String dsr1code,
                                 String checkdsrcode,String diakinisidsrcode, String checkmetaggisidsrcode,String metaggisidsrcode,
                                 String dsr0num,String dsr1num,String checkdsrnum,String diakinisidsrnum,String checkmetaggisidsrnum,String metaggisidsrnum,String printer,String salesmanid)

    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("clientid", clientid);
        contentValues.put("vendorwerks", vendorwerks);
        contentValues.put("webserviceurl", webserviceurl);
        contentValues.put("dsr0code", dsr0code);
        contentValues.put("dsr1code", dsr1code);
        contentValues.put("checkdsrcode", checkdsrcode);
        contentValues.put("diakinisidsrcode", diakinisidsrcode);
        contentValues.put("metaggisidsrcode", metaggisidsrcode);
        contentValues.put("checkmetaggisidsrcode", checkmetaggisidsrcode);
        contentValues.put("dsr0num", dsr0num);
        contentValues.put("dsr1num", dsr1num);
        contentValues.put("checkdsrnum", checkdsrnum);
        contentValues.put("diakinisidsrnum", diakinisidsrnum);
        contentValues.put("metaggisidsrnum", metaggisidsrnum);
        contentValues.put("checkmetaggisidsrnum", checkmetaggisidsrnum);
        contentValues.put("printer", printer);
        contentValues.put("salsmid", salesmanid);

        db.insert("config", null, contentValues);
        return true;
    }


        public boolean insertCompany (String name,String address, String city,String zipcode,String afm,String doy,
                                 String phone1, String phone2,String fax,String argemi)

    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("zipcode", zipcode);
        contentValues.put("afm", afm);
        contentValues.put("doy", doy);
        contentValues.put("phone1", phone1);
        contentValues.put("phone2", phone2);
        contentValues.put("fax", fax);
        contentValues.put("argemi", argemi);

        db.insert("company", null, contentValues);
        return true;
    }

    public Cursor getRoutesData(String vendorwerks) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select descr,codeid from shipvia where isvalid='1' and codeid like '" + vendorwerks + "%' order by descr", null );
        return res;
    }

    public Cursor getFactoriesData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from factories order by descr", null );
        return res;
    }

    public Cursor getDiakinisiData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from diakinisi", null );
        return res;
    }

    public Cursor getRouteData(String codeid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select descr,codeid from shipvia where isvalid='1' and codeid='" + codeid + "'", null );
        return res;
    }

    public Cursor getSalesmenData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select name,salsmid,code from salesman where isvalid='1' order by name", null );
        return res;
    }

    public Cursor getTransportationsData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select descr,codeid from transportation where isvalid='1' order by descr", null );
        return res;
    }

    public Cursor getSuppliersData(String name,String code,String afm,String phone11){

        String wherename="";
        String wherecode="";
        String whereafm="";
        String wherephone11="";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        if (name.length() > 0){
            wherename=" and (supplier.name like '%" + name + "%') ";
        }

        if (code.length() > 0){
            wherecode=" and (supplier.code like '%" + code + "%') ";
        }

        if (afm.length() > 0){
            whereafm=" and (supplier.afm like '%" + afm + "%') ";
        }

        if (phone11.length() > 0){
            wherephone11=" and (supplier.phone11 like '%" + phone11 + "%') ";
        }


        res =  db.rawQuery( "select supplier.id,supplier.supid,supplier.code,supplier.name,supplier.afm,supplier.phone11 from supplier " +
                "where supplier.isvalid='1' " + wherename + wherecode + whereafm + wherephone11, null );


            return res;

    }


    public Cursor getBarcodesData(String routecodeid,String pagolekani) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res;

        String wherepagolekani="";

        String whereroutecode=" and shipvia.codeid='" + routecodeid + "'";

        if (pagolekani.length() > 0){

            wherepagolekani=" and milksuptobarcode.zpagolekaniid2 like '%" + pagolekani + "%' ";

            res =  db.rawQuery( "select supplier.name as station,supplier.supid as stationid,milksuptobarcode.zpagolekaniid2 as barcode from supplier " +
                    "inner join shipvia on supplier.shvid=shipvia.codeid " +
                    "inner join milksuptosup on supplier.supid=milksuptosup.supid " +
                    "inner join milksuptobarcode on milksuptosup.zstsid=milksuptobarcode.zstsid " +
                    " where supplier.isvalid='1' and milksuptosup.isvalid='1' and milksuptobarcode.isvalid='1' "  + wherepagolekani + " order by supplier.name,milksuptobarcode.zpagolekaniid2", null );

        }
        else
        {
            res =  db.rawQuery( "select supplier.name as station,supplier.supid as stationid,milksuptobarcode.zpagolekaniid2 as barcode from supplier " +
                    "inner join shipvia on supplier.shvid=shipvia.codeid " +
                    "inner join milksuptosup on supplier.supid=milksuptosup.supid " +
                    "inner join milksuptobarcode on milksuptosup.zstsid=milksuptobarcode.zstsid " +
                    " where supplier.isvalid='1' and milksuptosup.isvalid='1' and milksuptobarcode.isvalid='1' " + whereroutecode + " order by supplier.name,milksuptobarcode.zpagolekaniid2", null );

        }





        return res;
    }

    public Cursor getConfigData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from config where id="+id+"", null );
        return res;
    }

    public Cursor getCompanyData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from company where id="+id+"", null );
        return res;
    }


    public Cursor getExtendConfigData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select config.*,transportation.descr as transportation,salesman.name as driver from config " +
                " left outer join salesman on config.salsmid=salesman.salsmid " +
                " left outer join transportation on config.transpid=transportation.codeid " +
                " where config.id="+id+"", null );
        return res;
    }


    public boolean alterConfigData(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("alter table config add column routeid text");

        return true;
    }

    public boolean alterTrades(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("alter table trades add column xlm text");

        return true;
    }

    public Cursor getDocseriesData(String docseries){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from docseries where docseries='" + docseries + "'", null );

        return res;
    }


    public Cursor getSupToSupData(String zstsid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from milksuptosup where zstsid='"+zstsid+"'", null );
        return res;
    }
    public Cursor getSupToSupLinesData(String zstslinesid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from milksuptosuplines where zstslinesid='"+zstslinesid+"'", null );
        return res;
    }
    public Cursor geSupToSupLines2Data(String zstslines2id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from milksuptosuplines2 where zstslines2id='"+zstslines2id+"'", null );
        return res;
    }
    public Cursor getSupToBarcodeData(String zstsid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from milksuptobarcode where zstsid='" + zstsid + "'", null);
        return res;
    }

    public Cursor getSalesmanData(String salsmcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from salesman where code='" + "0000" + salsmcode + "'", null);
        return res;
    }

    public Cursor getTransportationData(String codeid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from transportation where codeid='" + codeid + "'", null);
        return res;
    }

    public Cursor getShipviaData(String codeid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from shipvia where codeid='" + codeid + "'", null);
        return res;
    }




    public Cursor getBarcodeData(String barcode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select shipvia.codeid as rtcodeid from supplier " +
                " inner join shipvia on supplier.shvid=shipvia.codeid " +
                " inner join milksuptosup on supplier.supid=milksuptosup.supid " +
                " inner join milksuptobarcode on milksuptosup.zstsid=milksuptobarcode.zstsid " +
                " where milksuptobarcode.zpagolekaniid2='"+ barcode + "' and supplier.isvalid='1' and milksuptosup.isvalid='1' and milksuptobarcode.isvalid='1'", null );

        return res;
    }


    public Cursor getCusromerId(String stoid, String secstoid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select rtc.rcustid " +
                " from  retailcustomer as rtc " +
                " inner join diakinisi as diak on rtc.code=diak.customer " +
                " where diak.stoid='" + stoid + "' and diak.secstoid='" + secstoid + "'", null );
        return res;
    }


    public Cursor getCusromerIdFromCode(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select rtc.rcustid " +
                " from  retailcustomer as rtc " +
                " where  rtc.code='" + code + "'", null );
        return res;
    }


    public Cursor getRetailCusromers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from retailcustomer", null );
        return res;
    }

    public Cursor getRecordData(String tablename, int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " +tablename+ " where id="+id+"", null );
        return res;
    }

    public Cursor getBarcodeFarmersData(String barcode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select supplier.supid,supplier.name as supplier,retailmaterial.iteid,retailmaterial.descr as milktype from milksuptobarcode " +
                "inner join milksuptosup on milksuptobarcode.zstsid=milksuptosup.zstsid " +
                "inner join milksuptosuplines on milksuptosuplines.zstsid=milksuptosup.zstsid " +
                "inner join supplier on milksuptosuplines.supid=supplier.supid " +
                "inner join milksuptosuplines2 on milksuptosuplines2.zstsid=supplier.supid " +
                "inner join retailmaterial on milksuptosuplines2.iteid=retailmaterial.iteid " +
                "where milksuptobarcode.zpagolekaniid2='" + barcode + "' order by supplier.name,retailmaterial.descr", null );
        return res;
    }

    public Cursor getLastTodayCollection_aa(String todaydate,String routeid,String stationid,String pagolekani){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(cast(aa as integer)) as last_aa from allcollections where hmer='" + todaydate + "' and routeid='" + routeid + "' and stationid='" + stationid + "' and pagolekani='" + pagolekani + "'", null );
        return res;
    }

    public Cursor getLastTodayCollection(String todaydate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(id) as last_id from allcollections where hmer='" + todaydate + "' and iscanceled='0'", null );
        return res;
    }

    public Cursor getCollectionIscanceled(String todaydate,String aa,String routeid,String stationid,String pagolekani){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id,iscanceled from allcollections where aa='" + aa + "' and hmer='" + todaydate + "' and routeid='" + routeid + "' and stationid='" + stationid + "' and pagolekani='" + pagolekani + "'", null );
        return res;
    }


    public Cursor getCollectionId(String todaydate,String aa,String routeid,String stationid,String pagolekani){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id from allcollections where aa='" + aa + "' and hmer='" + todaydate + "' and routeid='" + routeid + "' and stationid='" + stationid + "' and pagolekani='" + pagolekani + "'", null );
        return res;
    }

    public int getCollectionRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows =(int) DatabaseUtils.longForQuery(db, "select count(*) from collection",null);
        return numRows;
    }
    public Cursor getTradeCheckIscanceled(String todaydate,String aa){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id,iscanceled from tradechecks where aa='" + aa + "' and hmer='" + todaydate + "'", null );

        return res;

    }

    public Cursor getTradeCheckId(String todaydate,String aa){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id from tradechecks where aa='" + aa + "' and hmer='" + todaydate + "'", null );
        return res;
    }

    public Cursor getLastTodayTradeCheck(String todaydate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(id) as last_id from tradechecks where hmer='" + todaydate + "'", null );
        return res;
    }

    public Cursor getNewTradesToCheck(String todaydate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id from trades where hmer='" + todaydate + "' and trchid='0' and iscancel='0' and iscanceled='0' and ischecked='0'", null );
        return res;
    }

    public Cursor getFromUptoTradecodesToCheck(String todaydate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(tradecode) as uptotradecode,min(tradecode) as fromtradecode from trades where hmer='" + todaydate + "' and trchid='0' and iscancel='0' and iscanceled='0' and ischecked='0'", null );
        return res;
    }


    public Cursor getLastTodayTradeCheck_aa(String todaydate){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(cast(aa as integer)) as last_aa from tradechecks where hmer='" + todaydate + "'", null );
        return res;
    }


    public Cursor getLastTradeIdOfTradeCheck(String trchkid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select max(id) as lasttradeid from trades where trchid='" + trchkid + "'", null );

        return res;

    }


    public Cursor getLastTradeDataOfTradeCheck(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from trades where id='" + trid + "'", null );
        return res;
    }

    public int getNonUpdatedTrades(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows =(int) DatabaseUtils.longForQuery(db, "select count(*) from trades where updated='0'",null);
        return numRows;
    }


    public int getTradeLinesRows(String trid){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows =(int) DatabaseUtils.longForQuery(db, "select count(*) from tradelines where trid='" + trid + "'",null);
        return numRows;
    }

    public int getTradeLineRowCount(String aa,String trid){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows =(int) DatabaseUtils.longForQuery(db, "select count(*) from tradelines where aa='" + aa + "' and trid='" + trid + "'",null);
        return numRows;
    }



    public int getSuppliers(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows =(int) DatabaseUtils.longForQuery(db, "select count(*) from supplier",null);
        return numRows;
    }

    public boolean existsTransportation(String codeid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from transportation where codeid='" + codeid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsShipvia(String codeid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from shipvia where codeid='" + codeid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsSalesman(String salsmid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from salesman where salsmid='" + salsmid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsSupToBarcode(String zstsid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from milksuptobarcode where zstsid='" + zstsid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsSupToSupLines2(String zstslines2id,String comid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from milksuptosuplines2 where zstslines2id='" + zstslines2id + "' and comid='" + comid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsSupToSupLines(String zstslinesid,String comid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from milksuptosuplines where zstslinesid='" + zstslinesid + "' and comid='" + comid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsSupToSup(String zstsid,String comid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from milksuptosup where zstsid='" + zstsid + "' and comid='" + comid + "'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsDocseries(String domaintype,String codeid,String docseries){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from docseries where domaintype='" + domaintype + "' and codeid='" + codeid + "' and docseries='" + docseries + "'",null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsMaterial(String iteid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from retailmaterial where iteid='" + iteid +"'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsSupplier(String supid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from supplier where supid='" + supid +"'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }

    public boolean existsCustomer(String rcustid){

        SQLiteDatabase db = this.getReadableDatabase();

        if ((int) DatabaseUtils.longForQuery(db, "select count(*) from retailcustomer where rcustid='" + rcustid +"'" ,null) > 0) {

            return true;

        }
        else
        {
            return false;
        }

    }


    public int numberOfTableRows(String tablename){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, tablename);
        return numRows;
    }

    public boolean updateConfiglastdsr(Integer id, String dsr1num)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dsr1num", dsr1num);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


    public boolean updateConfiglastChkdsr(Integer id, String checkdsrnum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("checkdsrnum", checkdsrnum);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateConfiglastMetagdsr(Integer id, String metaggisidsrnum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("metaggisidsrnum", metaggisidsrnum);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateConfiglastMetagChkdsr(Integer id, String checkmetaggisidsrnum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("checkmetaggisidsrnum", checkmetaggisidsrnum);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateConfiglastDiakinisidsr(Integer id, String diakinisidsrnum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("diakinisidsrnum", diakinisidsrnum);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }



    public boolean updateConfig (Integer id, String clientid,String vendorwerks, String webserviceurl,String dsr0code,String dsr1code,
                                     String checkdsrcode,String diakinisidsrcode, String checkmetaggisidsrcode,String metaggisidsrcode,
                                     String dsr0num,String dsr1num,String checkdsrnum,String diakinisidsrnum,String checkmetaggisidsrnum,String metaggisidsrnum,String printer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("clientid", clientid);
        contentValues.put("vendorwerks", vendorwerks);
        contentValues.put("webserviceurl", webserviceurl);
        contentValues.put("dsr0code", dsr0code);
        contentValues.put("dsr1code", dsr1code);
        contentValues.put("checkdsrcode", checkdsrcode);
        contentValues.put("diakinisidsrcode", diakinisidsrcode);
        contentValues.put("metaggisidsrcode", metaggisidsrcode);
        contentValues.put("checkmetaggisidsrcode", checkmetaggisidsrcode);
        contentValues.put("dsr0num", dsr0num);
        contentValues.put("dsr1num", dsr1num);
        contentValues.put("checkdsrnum", checkdsrnum);
        contentValues.put("diakinisidsrnum", diakinisidsrnum);
        contentValues.put("metaggisidsrnum", metaggisidsrnum);
        contentValues.put("checkmetaggisidsrnum", checkmetaggisidsrnum);
        contentValues.put("printer", printer);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateCompany (Integer id,String name,String address, String city,String zipcode,String afm,String doy,
                                  String phone1, String phone2,String fax,String argemi)

    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("zipcode", zipcode);
        contentValues.put("afm", afm);
        contentValues.put("doy", doy);
        contentValues.put("phone1", phone1);
        contentValues.put("phone2", phone2);
        contentValues.put("fax", fax);
        contentValues.put("argemi", argemi);

        db.update("company", contentValues, "id = ? ", new String[] { Integer.toString(id) } );

        return true;
    }

    public boolean updateLastTransportationRv  (Integer id, String last_transportation_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_transportation_rv", last_transportation_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastShipviaRv  (Integer id, String last_shipvia_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_shipvia_rv", last_shipvia_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastSalesmanRv  (Integer id, String last_salesman_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_salesman_rv", last_salesman_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastSupToBarcodeRv  (Integer id, String last_suptobarcode_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_suptobarcode_rv", last_suptobarcode_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastSupToSupLines2Rv  (Integer id, String last_suptosuplines2_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_suptosuplines2_rv", last_suptosuplines2_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastSupToSupLinesRv  (Integer id, String last_suptosuplines_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_suptosuplines_rv", last_suptosuplines_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


    public boolean updateLastSupToSupRv  (Integer id, String last_suptosup_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_suptosup_rv", last_suptosup_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastDocseriesRv  (Integer id, String last_docseries_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_docseries_rv", last_docseries_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public boolean updateLastCustomersRv  (Integer id, String last_docseries_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_docseries_rv", last_docseries_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateLastMaterialRv  (Integer id, String last_meterial_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_meterial_rv", last_meterial_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

   public boolean updateLastSupplierRv (Integer id, String last_sup_rv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_sup_rv", last_sup_rv);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


    public boolean updateRoute (Integer id, String routeid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("routeid", routeid);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateTransportation (Integer id, String transportationid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("transpid", transportationid);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateConfigSalesman (Integer id, String salesmanid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salsmid", salesmanid);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateTranspAndSalesm (Integer id, String transpid,String salsmid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("transpid", transpid);
        contentValues.put("salsmid", salsmid);

        db.update("config", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean InsertCollectionTrade(String cid,String domaintype,String collid, String docseries,String docnum,String tradecode,String hmer,String time,
                                         String supid,String shvid,String zpagolekaniid2,String zfat,String ztemperature,String zph,String notes,String trsid,
                                         String salesmanid, String dromologionum,String iscancel,String iscanceled,String ischecked)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", cid);
        contentValues.put("domaintype", domaintype);
        contentValues.put("collid", collid);
        contentValues.put("trchid", "0");
        contentValues.put("docseries", docseries);
        contentValues.put("docnum", docnum);
        contentValues.put("tradecode", tradecode);
        contentValues.put("hmer", hmer);
        contentValues.put("time", time);
        contentValues.put("supid", supid);
        contentValues.put("shvid", shvid);
        contentValues.put("zpagolekaniid2", zpagolekaniid2);
        contentValues.put("zfat", zfat);
        contentValues.put("ztemperature", ztemperature);
        contentValues.put("zph", zph);
        contentValues.put("notes", notes);
        contentValues.put("trsid", trsid);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("dromologionum", dromologionum);
        contentValues.put("iscanceled", iscanceled);
        contentValues.put("iscancel", iscancel);
        contentValues.put("ischecked", ischecked);
        contentValues.put("isdiakinisi", "0");
        contentValues.put("ismetaggisi", "0");
        contentValues.put("ismetaggisicheck", "0");
        contentValues.put("updated", "0");

        db.insert("trades", null, contentValues);
        return true;
    }

    public boolean InsertTradeCheckTrade(String cid,String domaintype,String trchkid, String docseries,String docnum,String tradecode,String hmer,String time,
                                         String trsid,String salesmanid,String fromtradecode,String uptotradecode,String dromologionum,String shvid,String xlm,String notes,String iscancel,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", cid);
        contentValues.put("domaintype", domaintype);
        contentValues.put("collid", "0");
        contentValues.put("trchid", trchkid);
        contentValues.put("docseries", docseries);
        contentValues.put("docnum", docnum);
        contentValues.put("tradecode", tradecode);
        contentValues.put("hmer", hmer);
        contentValues.put("time", time);
        contentValues.put("supid", trsid);
        contentValues.put("trsid", trsid);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("fromtradecode", fromtradecode);
        contentValues.put("uptotradecode", uptotradecode);
        contentValues.put("dromologionum", dromologionum);
        contentValues.put("shvid", shvid);
        contentValues.put("xlm", xlm);
        contentValues.put("notes", notes);
        contentValues.put("iscancel", iscancel);
        contentValues.put("iscanceled", iscanceled);
        contentValues.put("isdiakinisi", "0");
        contentValues.put("ismetaggisi", "0");
        contentValues.put("ismetaggisicheck", "0");
        contentValues.put("updated", "0");

        db.insert("trades", null, contentValues);
        return true;
    }


    public boolean InsertMetaggisiTradeCheckTrade(String cid,String domaintype,String trchkid, String docseries,String docnum,String tradecode,
                                                  String hmer,String time,String trsid,String salesmanid,String fromtradecode,String uptotradecode,String dromologionum,String shvid,
                                                  String newmetaggisidsrno,String fromkeb,String tokeb,String fromtransportation,String secstoid,String iscancel,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", cid);
        contentValues.put("domaintype", domaintype);
        contentValues.put("collid", "0");
        contentValues.put("trchid", trchkid);
        contentValues.put("docseries", docseries);
        contentValues.put("docnum", docnum);
        contentValues.put("tradecode", tradecode);
        contentValues.put("hmer", hmer);
        contentValues.put("time", time);
        contentValues.put("supid", trsid);
        contentValues.put("trsid", trsid);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("fromtradecode", fromtradecode);
        contentValues.put("uptotradecode", uptotradecode);
        contentValues.put("dromologionum", dromologionum);
        contentValues.put("shvid", shvid);
        contentValues.put("daafrom", newmetaggisidsrno);
        contentValues.put("daato", newmetaggisidsrno);
        contentValues.put("fromkeb", fromkeb);
        contentValues.put("tokeb", tokeb);
        contentValues.put("secjustificationfrom", fromtransportation);
        contentValues.put("secstoid", secstoid);
        contentValues.put("iscancel", iscancel);
        contentValues.put("iscanceled", iscanceled);
        contentValues.put("isdiakinisi", "0");
        contentValues.put("ismetaggisi", "0");
        contentValues.put("ismetaggisicheck", "1");
        contentValues.put("updated", "0");


        db.insert("trades", null, contentValues);
        return true;
    }


        public boolean InsertMetaggisiTrade(String cid,String domaintype,String trchkid, String docseries,String docnum,String tradecode,
                                                  String hmer,String time,String trsid,String salesmanid,String fromtradecode,String uptotradecode,String dromologionum,String shvid,
                                                  String fromkeb,String tokeb,String fromtransportation,String custid,String iscancel,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", cid);
        contentValues.put("domaintype", domaintype);
        contentValues.put("collid", "0");
        contentValues.put("trchid", trchkid);
        contentValues.put("docseries", docseries);
        contentValues.put("docnum", docnum);
        contentValues.put("tradecode", tradecode);
        contentValues.put("hmer", hmer);
        contentValues.put("time", time);
        contentValues.put("supid", trsid);
        contentValues.put("trsid", trsid);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("fromtradecode", fromtradecode);
        contentValues.put("uptotradecode", uptotradecode);
        contentValues.put("dromologionum", dromologionum);
        contentValues.put("shvid", shvid);
        contentValues.put("fromkeb", fromkeb);
        contentValues.put("tokeb", tokeb);
        contentValues.put("secjustificationfrom", fromtransportation);
        contentValues.put("custid", custid);
        contentValues.put("iscancel", iscancel);
        contentValues.put("iscanceled", iscanceled);
        contentValues.put("isdiakinisi", "0");
        contentValues.put("ismetaggisi", "1");
        contentValues.put("ismetaggisicheck", "0");
        contentValues.put("updated", "0");

        db.insert("trades", null, contentValues);
        return true;
    }


    public boolean InsertDiakinisiTrade(String cid,String domaintype,String trchkid, String docseries,String docnum,String tradecode,String hmer,String time,
                                        String trsid,String salesmanid,String fromtradecode,String uptotradecode,String dromologionum,String shvid,
                                        String fromkeb,String stoid,String secstoid,String custid,String notes,String iscancel,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", cid);
        contentValues.put("domaintype", domaintype);
        contentValues.put("collid", "0");
        contentValues.put("trchid", trchkid);
        contentValues.put("docseries", docseries);
        contentValues.put("docnum", docnum);
        contentValues.put("tradecode", tradecode);
        contentValues.put("hmer", hmer);
        contentValues.put("time", time);
        contentValues.put("supid", trsid);
        contentValues.put("trsid", trsid);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("fromtradecode", fromtradecode);
        contentValues.put("uptotradecode", uptotradecode);
        contentValues.put("dromologionum", dromologionum);
        contentValues.put("shvid", shvid);
        contentValues.put("fromkeb", fromkeb);
        contentValues.put("stoid", stoid);
        contentValues.put("secstoid", secstoid);
        contentValues.put("custid", custid);
        contentValues.put("notes", notes);
        contentValues.put("iscancel", iscancel);
        contentValues.put("iscanceled", iscanceled);
        contentValues.put("isdiakinisi", "1");
        contentValues.put("ismetaggisi", "0");
        contentValues.put("ismetaggisicheck", "0");
        contentValues.put("updated", "0");


        db.insert("trades", null, contentValues);
        return true;
    }

    public boolean InsertCollectionTradeLines(String aa,String trid,String iteid,String qty, String z_wpid,String z_autonum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("aa", aa);
        contentValues.put("trid", trid);
        contentValues.put("iteid", iteid);
        contentValues.put("qty", qty);
        contentValues.put("z_wpid", z_wpid);
        contentValues.put("z_autonum", z_autonum);

        db.insert("tradelines", null, contentValues);
        return true;
    }


    public boolean InsertPermanentCollection(String hmer,String aa,String routeid,String stationid,String pagolekani,String barcode,
                                             String fat,String temperature,String ph,String amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hmer", hmer);
        contentValues.put("aa", aa);
        contentValues.put("routeid", routeid);
        contentValues.put("stationid", stationid);
        contentValues.put("pagolekani", pagolekani);
        contentValues.put("barcode",barcode);
        contentValues.put("fat", fat);
        contentValues.put("temperature", temperature);
        contentValues.put("ph", ph);
        contentValues.put("amount", amount);
        contentValues.put("iscanceled", "0");

        db.insert("allcollections", null, contentValues);
        return true;
    }




    public boolean InsertCollection(Integer id,String routeid,String stationid,
                                    String pagolekani,String amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("routeid", routeid);
        contentValues.put("stationid", stationid);
        contentValues.put("pagolekani", pagolekani);
        contentValues.put("barcode", "");
        contentValues.put("fat", "");
        contentValues.put("temperature", "");
        contentValues.put("ph", "");
        contentValues.put("amount", amount);

        db.insert("collection", null, contentValues);
        return true;
    }

    public boolean UpdateCollectionQuantity(Integer id,String amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount", amount);
        db.update("collection", contentValues, "id = ? ", new String[] { Integer.toString(id)} );
        return true;
    }

    public boolean UpdateCollectionQuality(Integer id,String barcode,String fat,String temperature,String ph)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("barcode", barcode);
        contentValues.put("fat", fat);
        contentValues.put("temperature", temperature);
        contentValues.put("ph", ph);

        db.update("collection", contentValues, "id = ? ", new String[] { Integer.toString(id)} );
        return true;
    }

    public boolean UpdatePermanetCollectionIscancel(String id,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("iscanceled", iscanceled);
        db.update("allcollections", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public boolean UpdateCanceledCollectionTrades(String collid,String iscanceled) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update trades set iscanceled='" + iscanceled + "'" +
                " where trades.collid='" + collid + "' and trades.iscancel='0' and trades.iscanceled='0'");
        return true;
    }


    public boolean UpdateCanceledCheckTradeTrades(String trchid,String iscanceled) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update trades set iscanceled='" + iscanceled + "'" +
                " where trades.trchid='" + trchid + "' and trades.isdiakinisi='0' and trades.ismetaggisi='0' and trades.ismetaggisicheck='0' and trades.iscancel='0' and trades.iscanceled='0'");
        return true;
    }

    public boolean UpdateCanceledDiakinisiTrades(String trchid,String iscanceled) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update trades set iscanceled='" + iscanceled + "'" +
                " where trades.trchid='" + trchid + "' and trades.isdiakinisi='1' and trades.ismetaggisi='0' and trades.ismetaggisicheck='0' and trades.iscancel='0' and trades.iscanceled='0'");
        return true;
    }


    public boolean UpdateUploadedTrades(String updatedtrades) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update trades set updated='1'" +
                " where trades.id in (" + updatedtrades + ")");
        return true;
    }

    public boolean InsertCollectionLines(String aa,String supid,
                                    String iteid,String remarks,String amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("aa", aa);
        contentValues.put("supid", supid);
        contentValues.put("iteid", iteid);
        contentValues.put("remarks",remarks);
        contentValues.put("amount", amount);


        db.insert("collectionlines", null, contentValues);
        return true;
    }


    public boolean InsertCollectionChamberLines(String aa,String amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chamberid", aa);
        contentValues.put("amount", amount);
        db.insert("collectionchamberlines", null, contentValues);
        return true;
    }

    public boolean InsertTradeCheck(String hmer,String aa,String fromtradecode,String uptotradecode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hmer", hmer);
        contentValues.put("aa", aa);
        contentValues.put("fromtradecode", fromtradecode);
        contentValues.put("uptotradecode", uptotradecode);
        contentValues.put("iscanceled", "0");

        db.insert("tradechecks", null, contentValues);
        return true;
    }

    public boolean InsertMetaggisiTradeLines(String aa,String trid,String iteid,String qty, String z_wpid,String z_autonum,String fromzwpid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("aa", aa);
        contentValues.put("trid", trid);
        contentValues.put("iteid", iteid);
        contentValues.put("qty", qty);
        contentValues.put("z_wpid", z_wpid);
        contentValues.put("z_autonum", z_autonum);
        contentValues.put("fromzwpid", fromzwpid);

        db.insert("tradelines", null, contentValues);

        return true;
    }


    public boolean InsertTradeCheckTradeLines(String aa,String trid,String iteid,String qty, String z_wpid,String z_autonum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("aa", aa);
        contentValues.put("trid", trid);
        contentValues.put("iteid", iteid);
        contentValues.put("qty", qty);
        contentValues.put("z_wpid", z_wpid);
        contentValues.put("z_autonum", z_autonum);

        db.insert("tradelines", null, contentValues);

        return true;
    }


    public boolean insertDestinationTransferChamberdata(String destinationchamberid,String initialqty,String transqty,String sample)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("destinationchamberid", destinationchamberid);
        contentValues.put("initialqty", initialqty);
        contentValues.put("transqty", transqty);
        contentValues.put("sample", sample);

        db.insert("destinationtransferdata", null, contentValues);

        return true;
    }
    public boolean updateDestinationTransferChamberdata(String destinationchamberid,String initialqty,String transqty,String sample)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("initialqty", initialqty);
        contentValues.put("transqty", transqty);
        contentValues.put("sample", sample);

        db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[] { destinationchamberid } );

        return true;
    }
      
    public boolean updateDestinationTransferedQuantities(int transqty1,int transqty2,int transqty3,int transqty4,int transqty5,int transqty6,int transqty7,int transqty8,
                                                         int transqty9,int transqty10,int transqty11,int transqty12,int transqty13,int transqty14,int transqty15)
    {
        SQLiteDatabase db = this.getWritableDatabase();
       
        for (int i=1;i<=15;i++) {

            if (i==1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty1);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==2) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty2);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==3) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty3);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==4) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty4);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==5) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty5);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==6) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty6);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==7) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty7);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==8) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty8);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==9) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty9);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==10) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty10);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==11) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty11);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==12) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty12);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==13) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty13);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==14) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty14);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }
            if (i==15) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("transqty", transqty15);

                db.update("destinationtransferdata", contentValues, "destinationchamberid = ? ", new String[]{Integer.toString(i)});
            }



        }

        return true;
    }


    public boolean insertTransferSourceTrade(String cid,String domaintype,String xrisi,String docseries,String docnum,String tradecode,String hmer,
                                             String salesmanid,String notes,String supid,String trsid,String shvid,String dromologionum,String fromtradecode,String uptotradecode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cid", cid);
        contentValues.put("domaintype", domaintype);
        contentValues.put("xrisi", xrisi);
        contentValues.put("docseries", docseries);
        contentValues.put("docnum", docnum);
        contentValues.put("tradecode", tradecode);
        contentValues.put("hmer", hmer);
        contentValues.put("salesmanid", salesmanid);
        contentValues.put("notes", notes);
        contentValues.put("supid", supid);
        contentValues.put("trsid", trsid);
        contentValues.put("shvid", shvid);
        contentValues.put("dromologionum", dromologionum);
        contentValues.put("fromtradecode", fromtradecode);
        contentValues.put("uptotradecode", uptotradecode);

        db.insert("transfersourcetrade", null, contentValues);

        return true;
    }

    public boolean insertTransferChamberLines(String aa,String iteid,String sourceqty,String sourcesample,String sourcechamberid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("aa", aa);
        contentValues.put("iteid", iteid);
        contentValues.put("sourceqty", sourceqty);
        contentValues.put("sourcesample", sourcesample);
        contentValues.put("sourcechamberid", sourcechamberid);

        db.insert("transferchamberlines", null, contentValues);

        return true;
    }

    public boolean insertSourceTransferChamberdata(String sourcechamberid,String sourcesample,String  transqty,String destinationchamp1qty,String destinationchamp2qty,String destinationchamp3qty,
                                                   String destinationchamp4qty,String destinationchamp5qty,String destinationchamp6qty,String destinationchamp7qty,
                                                   String destinationchamp8qty,String destinationchamp9qty,String destinationchamp10qty,String destinationchamp11qty,
                                                   String destinationchamp12qty,String destinationchamp13qty,String destinationchamp14qty,String destinationchamp15qty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sourcechamberid", sourcechamberid);
        contentValues.put("sourcesample", sourcesample);
        contentValues.put("transqty", transqty);
        contentValues.put("destinationchamp1qty", destinationchamp1qty);
        contentValues.put("destinationchamp2qty", destinationchamp2qty);
        contentValues.put("destinationchamp3qty", destinationchamp3qty);
        contentValues.put("destinationchamp4qty", destinationchamp4qty);
        contentValues.put("destinationchamp5qty", destinationchamp5qty);
        contentValues.put("destinationchamp6qty", destinationchamp6qty);
        contentValues.put("destinationchamp7qty", destinationchamp7qty);
        contentValues.put("destinationchamp8qty", destinationchamp8qty);
        contentValues.put("destinationchamp9qty", destinationchamp9qty);
        contentValues.put("destinationchamp10qty", destinationchamp10qty);
        contentValues.put("destinationchamp11qty", destinationchamp11qty);
        contentValues.put("destinationchamp12qty", destinationchamp12qty);
        contentValues.put("destinationchamp13qty", destinationchamp13qty);
        contentValues.put("destinationchamp14qty", destinationchamp14qty);
        contentValues.put("destinationchamp15qty", destinationchamp15qty);

        db.insert("sourcetransferchamberdata", null, contentValues);

        return true;
    }

    public boolean updateSourceTransferChamberdata(String sourcechamberid,String destinationChamberQuantityList[])
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("destinationchamp1qty", destinationChamberQuantityList[0]);
        contentValues.put("destinationchamp2qty", destinationChamberQuantityList[1]);
        contentValues.put("destinationchamp3qty", destinationChamberQuantityList[2]);
        contentValues.put("destinationchamp4qty", destinationChamberQuantityList[3]);
        contentValues.put("destinationchamp5qty", destinationChamberQuantityList[4]);
        contentValues.put("destinationchamp6qty", destinationChamberQuantityList[5]);
        contentValues.put("destinationchamp7qty", destinationChamberQuantityList[6]);
        contentValues.put("destinationchamp8qty", destinationChamberQuantityList[7]);
        contentValues.put("destinationchamp9qty", destinationChamberQuantityList[8]);
        contentValues.put("destinationchamp10qty", destinationChamberQuantityList[9]);
        contentValues.put("destinationchamp11qty", destinationChamberQuantityList[10]);
        contentValues.put("destinationchamp12qty", destinationChamberQuantityList[11]);
        contentValues.put("destinationchamp13qty", destinationChamberQuantityList[12]);
        contentValues.put("destinationchamp14qty", destinationChamberQuantityList[13]);
        contentValues.put("destinationchamp15qty", destinationChamberQuantityList[14]);

        db.update("sourcetransferchamberdata", contentValues, "sourcechamberid = ? ", new String[] { sourcechamberid } );

        return true;
    }

    public boolean UpdateTradeCheck(String id,String uptotradecode,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uptotradecode", uptotradecode);
        contentValues.put("iscanceled", iscanceled);

        db.update("tradechecks", contentValues, "id = ? ", new String[] { id } );

        return true;
    }

    public boolean UpdateTradeCheckIscancel(String id,String iscanceled)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("iscanceled", iscanceled);
        db.update("tradechecks", contentValues, "id = ? ", new String[] { id } );
        return true;
    }



    public boolean UpdateTradeCheckTradesIsChecked(String hmer,String chkid,String ischecked) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update trades set ischecked='" + ischecked + "',chkid='" + chkid + "' " +
                " where hmer='" + hmer + "' and trchid='0' and isdiakinisi='0' and ismetaggisi='0' and ismetaggisicheck='0' and iscancel='0' and iscanceled='0' and ischecked='0'");
        return true;
    }


    public boolean UpdateCanceledTradeCheckTradesIschecked(String hmer,String chkid,String ischecked) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("update trades set ischecked='" + ischecked + "' " +
                " where hmer='" + hmer + "' and trchid='0' and isdiakinisi='0' and ismetaggisi='0' and ismetaggisicheck='0' and iscancel='0' and iscanceled='0' and ischecked='1' and chkid='" + chkid + "'");
        return true;
    }

    public boolean InsertDiakinisiTradeLines(String aa,String trid,String iteid,String qty, String z_wpid,String z_autonum)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("aa", aa);
        contentValues.put("trid", trid);
        contentValues.put("iteid", iteid);
        contentValues.put("qty", qty);
        contentValues.put("z_wpid", z_wpid);
        contentValues.put("z_autonum", z_autonum);

        db.insert("tradelines", null, contentValues);

        return true;
    }


    public Cursor getSameBarcode(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select distinct tr.tradecode as tr_code,sup.name as sup_name, tr.hmer as tr_date " +
                " from trades as tr " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where tr.iscancel='0' and tr.iscanceled='0' and trl.z_autonum='" + barcode + "' order by tr.tradecode", null );
        return res;
    }




    public Cursor getChambersData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from collectionchamberlines", null );
        return res;
    }

    public Cursor getCollectionData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from collection where id='1'", null );
        return res;
    }

    public Cursor getCollectionLinesData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from collectionlines", null );
        return res;
    }


    public Cursor getCollectionsDataToCheck(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select coll.id as coll_id,coll.aa as coll_aa,coll.stationid as coll_stationid,st.name as coll_station,coll.routeid as coll_routeid,coll.pagolekani as coll_pagolekani," +
                "coll.barcode as coll_samplecode,coll.fat as coll_fat,coll.temperature as coll_temperature,coll.ph as coll_ph, " +
                " tr.tradecode as tr_code,sup.name as sup_name,mat.descr as mat_descr ,(select sum(cast(trls.qty as integer)) as q from tradelines as trls inner join trades as tr on trls.trid=tr.id  where tr.collid=coll.id) as coll_qty," +
                "(select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty,trl.aa as trl_aa,trl.z_wpid as trl_chamber,trl.qty as trl_qty " +
                " from allcollections as coll " +
                " inner join trades as tr on tr.collid=coll.id " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " inner join supplier as st on coll.stationid=st.supid "+
                " inner join milksuptosup on st.supid=milksuptosup.supid " +
                " inner join shipvia on st.shvid=shipvia.codeid " +
                " inner join milksuptobarcode on milksuptosup.zstsid=milksuptobarcode.zstsid " +
                "where tr.hmer='" + hmer + "' and tr.trchid='0' and tr.iscancel='0' and tr.iscanceled='0' and tr.ischecked='0'  order by coll.id desc", null );
        return res;
    }

    public Cursor getCollectionsData(String hmer,String stationid,String routeid,String pagolekani) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select coll.aa as coll_aa,coll.barcode as coll_samplecode,coll.fat as coll_fat,coll.temperature as coll_temperature,coll.ph as coll_ph, " +
                " tr.tradecode as tr_code,sup.name as sup_name,mat.descr as mat_descr ,(select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty,trl.aa as trl_aa,trl.z_wpid as trl_chamber,trl.qty as trl_qty " +
                " from allcollections as coll " +
                " inner join trades as tr on tr.collid=coll.id " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                "where tr.hmer='" + hmer + "' and coll.stationid='" + stationid + "' and coll.routeid='" + routeid + "' and coll.pagolekani='" + pagolekani + "' order by coll.id desc", null );
        return res;
    }


    public Cursor getCollectionTradesData(String collid,String iscancel) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.id as trid,tr.tradecode,tr.hmer,tr.time,sup.name as sup_name,sup.street1 as sup_street,sup.city1 as sup_city,sup.zipcode1 as sup_zipcode," +
                 "sup.afm as sup_afm,sup.doy as sup_doy,ds.docseriesdescr as docdescr,slsm.name as driver,transp.descr as tranportation, " +
                 "(select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty " +
                " from trades as tr " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join transportation as transp on tr.trsid=transp.codeid " +
                " inner join salesman as slsm on tr.salesmanid=slsm.salsmid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " where tr.collid='" + collid + "' and iscancel='" + iscancel + "'", null );
        return res;
    }



    public Cursor getCollectionTradesDataToCancel(String collid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.*,ds.cancelseries " +
                " from trades as tr " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " where tr.collid='" + collid + "' and tr.iscancel='0'", null );
        return res;
    }


    public Cursor getCollectionTradesIsChecked(String collid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.id as trid " +
                " from trades as tr " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " where tr.collid='" + collid + "' and iscancel='0' and ischecked='1'", null );
        return res;
    }

    public Cursor getCollectionTradeLines(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.aa as trl_aa,trl.qty as trl_qty,trl.z_autonum as trl_sample,trl.z_wpid as trl_chamber," +
                " mat.descr as mat_descr,mat.descr2 as mat_descr2,trl.iteid as trl_iteid " +
                " from tradelines as trl " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where trl.trid='" + trid + "'", null );
        return res;
    }

    public Cursor getGroupedTradeCheckTradeLines(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select sum(cast(trl.qty as imteger)) as trl_qty,max(trl.z_autonum) as trl_sample,trl.z_wpid as trl_chamber," +
                " mat.descr as mat_descr,mat.descr2 as mat_descr2,trl.iteid as trl_iteid " +
                " from tradelines as trl " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where trl.trid='" + trid + "'" +
                " group by trl.z_wpid,trl.iteid,mat.descr,mat.descr2 order by cast(trl.z_wpid as integer),mat.descr2", null );
        return res;
    }

    public Cursor getTradeData(String domaintype, String docseries,String docnum,String supid,String iscancel) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from trades where domaintype='" + domaintype + "' and docseries='" + docseries +
                "' and docnum='" + docnum + "' and supid='" + supid + "' and iscancel='" + iscancel + "'", null );
        return res;
    }

    public Cursor getDiakinisiTradeData(String domaintype, String docseries,String docnum,String custid,String iscancel) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select * from trades where domaintype='" + domaintype + "' and docseries='" + docseries +
                "' and docnum='" + docnum + "' and custid='" + custid + "' and iscancel='" + iscancel + "'", null );
        return res;
    }

    public Cursor getTradeChecksData(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trch.aa as trch_aa,trch.fromtradecode as trch_fromtradecode,trch.uptotradecode as trch_uptotradecode, " +
                " tr.tradecode as tr_tradecode,tr.fromtradecode as tr_fromtradecode,tr.uptotradecode as tr_uptotradecode,transp.descr as transp_descr, " +
                " mat.descr2 as mat_descr ,(select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty,trl.aa as trl_aa,trl.z_wpid as trl_chamber,trl.z_autonum as trl_sample,trl.qty as trl_qty " +
                " from tradechecks as trch " +
                " inner join trades as tr on tr.trchid=trch.id " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join transportation as transp on tr.supid=transp.codeid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where trch.hmer='" + hmer + "' order by trch.aa desc", null );
        return res;
    }

    public Cursor getGroupedTradeChecksData(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trch.aa as trch_aa,trch.fromtradecode as trch_fromtradecode,trch.uptotradecode as trch_uptotradecode, " +
                " tr.tradecode as tr_tradecode,tr.fromtradecode as tr_fromtradecode,tr.uptotradecode as tr_uptotradecode,transp.descr as transp_descr, " +
                " mat.descr2 as mat_descr ,trl.z_wpid as trl_chamber,tr.isdiakinisi as tr_isdiakinisi,tr.ismetaggisi as tr_ismetaggisi,tr.ismetaggisicheck as tr_ismetaggisicheck,tr.fromkeb as tr_fromkeb,tr.tokeb as tr_tokeb,fromfact.descr as tr_fromfactory,tofact.descr as tr_tofactory, " +
                " (select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty," +
                " max(trl.aa) as trl_aa,max(trl.z_autonum) as trl_sample,sum(cast(trl.qty as integer)) as trl_qty " +
                " from tradechecks as trch " +
                " inner join trades as tr on tr.trchid=trch.id " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join transportation as transp on tr.supid=transp.codeid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " left outer join factories as fromfact on tr.stoid=fromfact.codeid " +
                " left outer join factories as tofact on tr.secstoid=tofact.codeid " +
                " where trch.hmer='" + hmer + "' " +
                " group by trch.aa,tr.id,tr.tradecode,trch.fromtradecode,trch.uptotradecode,tr.fromtradecode,tr.uptotradecode,transp.descr,mat.descr2,trl.z_wpid,tr.isdiakinisi,tr.ismetaggisi,tr.ismetaggisicheck,tr.fromkeb,tr.tokeb,fromfact.descr,tofact.descr " +
                " order by trch.aa desc,tr.id,cast(trl.z_wpid as integer),mat.descr2", null );
        return res;
    }

    public Cursor getTradeCheckTradesData(String trchid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.id as trid,tr.tradecode,tr.hmer,tr.time,tr.isdiakinisi,tr.ismetaggisi,tr.ismetaggisicheck,tr.fromkeb,tr.tokeb,ds.docseriesdescr as docdescr,slsm.name as driver, " +
                "tr.fromtradecode as tr_fromtradecode,tr.uptotradecode as tr_uptotradecode,transp.descr as transp_descr,fromfact.branch as frombranch,tofact.branch as tobranch,fromfact.descr as fromfactory,tofact.descr as tofactory, " +
                "(select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty " +
                " from trades as tr " +
                " inner join transportation as transp on tr.supid=transp.codeid " +
                " inner join salesman as slsm on tr.salesmanid=slsm.salsmid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " left outer join factories as fromfact on tr.stoid=fromfact.codeid " +
                " left outer join factories as tofact on tr.secstoid=tofact.codeid " +
                " where tr.trchid='" + trchid + "'", null );
        return res;
    }

    public Cursor getTradeCheckTrade(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.id as trid,tr.tradecode,tr.hmer,tr.time,tr.isdiakinisi,tr.ismetaggisi,tr.ismetaggisicheck,tr.fromkeb,tr.tokeb,ds.docseriesdescr as docdescr,slsm.name as driver, " +
                "tr.fromtradecode as tr_fromtradecode,tr.uptotradecode as tr_uptotradecode,transp.descr as transp_descr,fromfact.branch as frombranch,tofact.branch as tobranch,fromfact.descr as fromfactory,tofact.descr as tofactory, " +
                "(select sum(cast(trls.qty as integer)) as q from tradelines as trls where trls.trid=tr.id) as tr_qty " +
                " from trades as tr " +
                " inner join transportation as transp on tr.supid=transp.codeid " +
                " inner join salesman as slsm on tr.salesmanid=slsm.salsmid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " left outer join factories as fromfact on tr.stoid=fromfact.codeid " +
                " left outer join factories as tofact on tr.secstoid=tofact.codeid " +
                " where tr.id='" + trid + "'", null );
        return res;
    }

    public Cursor getTradeCheckAmount(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select sum(cast(trl.qty as integer)) as amount " +
                " from trades as tr " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where tr.hmer='" + hmer + "' and tr.trchid='0' and tr.iscancel='0' and tr.iscanceled='0' and tr.ischecked='0'", null );
        return res;
    }


    public Cursor getTradeCheckChambersAmount(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.z_wpid as chamberid,sum(cast(trl.qty as integer)) as amount " +
                " from trades as tr " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where tr.hmer='" + hmer + "' and tr.trchid='0' and tr.iscancel='0' and tr.iscanceled='0' and tr.ischecked='0' group by trl.z_wpid order by cast(trl.z_wpid as integer)", null );
        return res;
    }

    public Cursor getSourceTransferChambersAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.sourcechamberid as chamberid,trl.sourcesample as sample,sum(cast(trl.sourceqty as integer)) as amount " +
                " from transferchamberlines as trl " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " group by trl.sourcechamberid,trl.sourcesample  " +
                " order by cast(trl.sourcechamberid as integer)", null );
        return res;
    }


    public Cursor getSourceTransferChambersLines() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.* " +
                " from transferchamberlines as trl " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " order by cast(trl.sourcechamberid as integer),trl.iteid", null );
        return res;
    }

    public Cursor getDestinationTransferChambersAmount(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.z_wpid as chamberid,sum(cast(trl.qty as integer)) as amount " +
                " from trades as tr " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where tr.id='" + trid + "' group by trl.z_wpid order by cast(trl.z_wpid as integer)", null );
        return res;
    }


    public Cursor getDestinationChambersAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select dtrd.destinationchamberid,dtrd.initialqty ,dtrd.transqty  " +
                " from destinationtransferdata as dtrd where dtrd.initialqty<>'0' or dtrd.transqty<>'0' " +
                " order by cast(dtrd.destinationchamberid as integer)", null );
        return res;
    }


    public Cursor getDestinationTransferTotalAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select sum(cast(dtrd.initialqty as integer)) as initialqty,sum(cast(dtrd.transqty as integer)) as transqty," +
                " sum(cast(dtrd.initialqty as integer)+cast(dtrd.transqty as integer)) as totaqty " +
                " from destinationtransferdata as dtrd" , null );
        return res;
    }


    public Cursor getSourceTotalChambersAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        res =  db.rawQuery("select sum(cast(strd.transqty as integer)) as totchambersamount " +
                " from sourcetransferchamberdata as strd", null );
        return res;
    }

    public Cursor getSourceChambersAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select strd.sourcechamberid,strd.transqty " +
                " from sourcetransferchamberdata as strd order by cast(strd.sourcechamberid as integer)", null );
        return res;
    }

    public Cursor getSourceChamberAmount(String sourcechamberid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select strd.* " +
                " from sourcetransferchamberdata as strd where strd.sourcechamberid='" + sourcechamberid + "'", null );
        return res;
    }

    public Cursor getTransferedChamberData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select strd.* " +
                " from sourcetransferchamberdata as strd order by cast(strd.sourcechamberid as integer)", null );
        return res;
    }

    public Cursor getTransferedChamberDataOfsourceChamber(String sourcechamberid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select strd.* " +
                " from sourcetransferchamberdata as strd where strd.sourcechamberid='" + sourcechamberid + "' order by cast(strd.sourcechamberid as integer)", null );
        return res;
    }


    public Cursor getTransferSourceTradeCheck() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select transtc.*,transp.descr as transdescr " +
                " from transfersourcetrade as transtc " +
                " inner join transportation as transp on transtc.trsid=transp.codeid ", null );
        return res;
    }

    public Cursor getNewTradeCheckLines(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.z_wpid,trl.iteid,sum(cast(trl.qty as integer)) as qty " +
                " from trades as tr " +
                " inner join tradelines as trl on trl.trid=tr.id " +
                " inner join supplier as sup on tr.supid=sup.supid " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where tr.hmer='" + hmer + "' and tr.trchid='0'  and tr.iscancel='0' and tr.iscanceled='0' and tr.ischecked='0' " +
                " group by trl.z_wpid,trl.iteid " +
                " order by cast(trl.z_wpid as integer),trl.iteid", null );
        return res;
    }


    public Cursor getTradeCheckTradesDataToCancel(String trchid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.*,ds.cancelseries " +
                " from trades as tr " +
                " inner join transportation as transp on tr.trsid=transp.codeid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " where tr.trchid='" + trchid + "' and tr.isdiakinisi='0' and tr.ismetaggisi='0' and tr.ismetaggisicheck='0' and tr.iscancel='0' and tr.iscanceled='0'", null );
        return res;
    }

    public Cursor getTradeCheckDiakinisiDataToCancel(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.*,ds.cancelseries " +
                " from trades as tr " +
                " inner join transportation as transp on tr.trsid=transp.codeid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " where tr.id='" + trid + "'", null );
        return res;
    }

    public Cursor getTradeCheckTradesDataforDiakinisi(String trchid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select tr.* " +
                " from trades as tr " +
                " inner join transportation as transp on tr.trsid=transp.codeid " +
                " inner join docseries as ds on tr.docseries=ds.docseries and tr.domaintype=ds.domaintype " +
                " where tr.trchid='" + trchid + "' and tr.isdiakinisi='0' and tr.ismetaggisi='0' and tr.ismetaggisicheck='0' and tr.iscancel='0' and tr.iscanceled='0'", null );
        return res;
    }


    public Cursor getTradeCheckTradeLines(String trid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trl.aa as trl_aa,trl.qty as trl_qty,trl.z_autonum as trl_sample,trl.z_wpid as trl_chamber,mat.descr as mat_descr,trl.iteid as trl_iteid " +
                " from tradelines as trl " +
                " inner join retailmaterial as mat on trl.iteid=mat.iteid " +
                " where trl.trid='" + trid + "'", null );
        return res;
    }




    public Cursor getTradesData(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trades.*,salesman.code as salesmancode from trades " +
                "inner join salesman on trades.salesmanid=salesman.salsmid " +
                "where hmer='" + hmer + "'", null );
        return res;
    }

    public Cursor getNotUpdatedTradesData(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery("select trades.*,salesman.code as salesmancode from trades " +
                "inner join salesman on trades.salesmanid=salesman.salsmid " +
                "where hmer='" + hmer +  "' and updated='0'", null );

        return res;
    }

    public Cursor getTradeLinesData(String hmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
       res =  db.rawQuery("select trl.*,tr.cid " +
               " from trades as tr " +
               " inner join tradelines as trl on trl.trid=tr.id " +
               " where tr.hmer='" + hmer + "'", null );
        return res;
    }



    public Boolean DeleteRowFromTable(String tablename,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablename, "id = ? ", new String[] { id });
        return true;
    }

    public Boolean DeleteTradeLines(String tablename,String trid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablename, "trid = ? ", new String[] { trid });
        return true;
    }

    public void deleteTable (String tablename)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablename,null,null);
    }



}
