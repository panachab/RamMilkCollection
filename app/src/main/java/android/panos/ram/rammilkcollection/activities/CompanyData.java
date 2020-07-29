package android.panos.ram.rammilkcollection.activities;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.panos.ram.rammilkcollection.R;
import android.panos.ram.rammilkcollection.models.DBHelper;
import android.panos.ram.rammilkcollection.models.GlobalClass;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CompanyData extends AppCompatActivity {

    private DBHelper mydb ;

    EditText name ;
    EditText address ;
    EditText city;
    EditText zipcode;
    EditText afm ;
    EditText doy;
    EditText phone1;
    EditText phone2;
    EditText fax;
    EditText argemi;


    int id_To_Display = 1;
    int UpdateCompany = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Στοιχεία Εταιρείας</small>"));

        name = (EditText) findViewById(R.id.editName);
        address = (EditText) findViewById(R.id.editAddress);
        city = (EditText) findViewById(R.id.editCity);
        zipcode = (EditText) findViewById(R.id.editZipcode);
        afm = (EditText) findViewById(R.id.editAfm);
        doy = (EditText) findViewById(R.id.editDoy);
        phone1 = (EditText) findViewById(R.id.editPhone1);
        phone2 = (EditText) findViewById(R.id.editPhone2);
        fax = (EditText) findViewById(R.id.editFax);
        argemi = (EditText) findViewById(R.id.editArgemi);

        final GlobalClass globalVariables = (GlobalClass) this.getApplicationContext();
        globalVariables.setcallingForm("Company");


        mydb = new DBHelper(this);


        Cursor rs = mydb.getCompanyData(id_To_Display);

        rs.moveToFirst();

        String m_name="" ;
        String m_address="" ;
        String m_city="";
        String m_zipcode="";
        String m_afm="";
        String m_doy="";
        String m_phone1="";
        String m_phone2="";
        String m_fax="";
        String m_argemi="";

        UpdateCompany=0;

        if (!rs.isAfterLast()) {

            m_name=rs.getString(rs.getColumnIndex("name"));
            m_address=rs.getString(rs.getColumnIndex("address"));
            m_city = rs.getString(rs.getColumnIndex("city"));
            m_zipcode = rs.getString(rs.getColumnIndex("zipcode"));
            m_afm=rs.getString(rs.getColumnIndex("afm"));
            m_doy = rs.getString(rs.getColumnIndex("doy"));
            m_phone1 = rs.getString(rs.getColumnIndex("phone1"));
            m_phone2 = rs.getString(rs.getColumnIndex("phone2"));
            m_fax = rs.getString(rs.getColumnIndex("fax"));
            m_argemi = rs.getString(rs.getColumnIndex("argemi"));


            UpdateCompany=1;
        }


        if (!rs.isClosed()) {
            rs.close();
        }



                name.setText((CharSequence) m_name);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                address.setText((CharSequence) m_address);
                address.setEnabled(true);
                address.setFocusableInTouchMode(true);
                address.setClickable(true);

                city.setText((CharSequence) m_city);
                city.setEnabled(true);
                city.setFocusableInTouchMode(true);
                city.setClickable(true);

                zipcode.setText((CharSequence) m_zipcode);
                zipcode.setEnabled(true);
                zipcode.setFocusableInTouchMode(true);
                zipcode.setClickable(true);

                afm.setText((CharSequence) m_afm);
                afm.setEnabled(true);
                afm.setFocusableInTouchMode(true);
                afm.setClickable(true);

                doy.setText((CharSequence) m_doy);
                doy.setEnabled(true);
                doy.setFocusableInTouchMode(true);
                doy.setClickable(true);

                phone1.setText((CharSequence) m_phone1);
                phone1.setEnabled(true);
                phone1.setFocusableInTouchMode(true);
                phone1.setClickable(true);

                phone2.setText((CharSequence) m_phone2);
                phone2.setEnabled(true);
                phone2.setFocusableInTouchMode(true);
                phone2.setClickable(true);

                fax.setText((CharSequence) m_fax);
                fax.setEnabled(true);
                fax.setFocusableInTouchMode(true);
                fax.setClickable(true);

                argemi.setText((CharSequence) m_argemi);
                argemi.setEnabled(true);
                argemi.setFocusableInTouchMode(true);
                argemi.setClickable(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            //onBackPressed();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return false;
        }
        else {
            return true;
        }
    }

    public void run(View view)
    {

          if (name.getText().toString().length()==0 || address.getText().toString().length()==0 || city.getText().toString().length()==0 || zipcode.getText().toString().length()==0 || afm.getText().toString().length()==0
                || doy.getText().toString().length()==0 || phone1.getText().toString().length()==0 || argemi.getText().toString().length()==0) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle(R.string.app_name);
            builder1.setMessage("Πρέπει να συμπληρώσετε όλα τα υποχρεωτικά πεδία των Στοιχείων της Εταιρείας!!");
            builder1.setIcon(R.mipmap.ic_launcher);
            builder1.setNegativeButton("OK", null);
            AlertDialog dialog = builder1.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);

          } else {

               if (UpdateCompany == 1) {

                      if (mydb.updateCompany(1, name.getText().toString(), address.getText().toString(),city.getText().toString(),zipcode.getText().toString(),afm.getText().toString(),
                              doy.getText().toString(),phone1.getText().toString(), phone2.getText().toString(),fax.getText().toString(),argemi.getText().toString())) {


                          Toast.makeText(getApplicationContext(), "Επιτυχής Αποθήκευση των Στοιχείων της Εταιρείας", Toast.LENGTH_SHORT).show();


                      } else {

                          Toast.makeText(getApplicationContext(), "Ανεπιτυχής Αποθήκευση των Στοιχείων της Εταιρείας", Toast.LENGTH_SHORT).show();
                      }

               } else {

                      if (mydb.insertCompany(name.getText().toString(),address.getText().toString(),city.getText().toString(),zipcode.getText().toString(),afm.getText().toString(),
                                  doy.getText().toString(),phone1.getText().toString(), phone2.getText().toString(),fax.getText().toString(),argemi.getText().toString())) {


                          Toast.makeText(getApplicationContext(), "Επιτυχής Αποθήκευση των Στοιχείων της Εταιρείας", Toast.LENGTH_SHORT).show();


                      } else {
                          Toast.makeText(getApplicationContext(), "Ανεπιτυχής Αποθήκευση των Στοιχείων της Εταιρείας", Toast.LENGTH_SHORT).show();
                      }

               }



      }
    }
}

