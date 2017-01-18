package com.ssrij.warnindia;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        toolbar.setTitle("About");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLicensesDialog(View v) {

        final Notices notices = new Notices();
        notices.addNotice(new Notice("Smart Location Library", "https://github.com/mrmans0n/smart-location-lib", "Nacho Lopez", new MITLicense()));
        notices.addNotice(new Notice("NanoTasks", "https://github.com/fabiendevos/nanotasks", "Fabien Devos", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Jsoup", "https://github.com/jhy/jsoup/", "Jonathan Hedley", new MITLicense()));
        notices.addNotice(new Notice("AVLoadingIndicatorView", "https://github.com/81813780/AVLoadingIndicatorView", "Jack Wang", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("MaterialList", "https://github.com/dexafree/MaterialList", "Dexafree", new MITLicense()));
        notices.addNotice(new Notice("SimpleCustomTabs", "https://github.com/eliseomartelli/SimpleCustomTabs", "Eliseo Martelli", new MITLicense()));
        notices.addNotice(new Notice("Permiso", "https://github.com/greysonp/permiso", "Greyson Parrelli", new MITLicense()));
        notices.addNotice(new Notice("Subsampling Scale Image View", "https://github.com/davemorrissey/subsampling-scale-image-view", "David Morrissey", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Picasso", "https://github.com/square/picasso", "Square Inc.", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Android Maps Extension", "https://github.com/mg6maciej/android-maps-extensions", "Maciej Górski", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("PrettyTime", "https://github.com/ocpsoft/prettytime/", "opcsoft", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("FABTransitionActivity", "https://github.com/coyarzun89/FabTransitionActivity", "Cristopher Oyarzún", new MITLicense()));
        notices.addNotice(new Notice("FABRevealLayout", "https://github.com/truizlop/FABRevealLayout", "Tomás Ruiz-López", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("LicensesDialog", "https://github.com/PSDev/LicensesDialog", "Philip Schiffer", new ApacheSoftwareLicense20()));

        new LicensesDialog.Builder(this)
                .setTitle("Open source software licenses")
                .setNotices(notices)
                .setIncludeOwnLicense(false)
                .build()
                .show();

    }

    public void showGovtWebsitesDialog(View v) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Data sources");
        builder.setMessage("INCOIS, Indian National Center for Ocean Information Services\nhttp://www.incois.gov.in/portal/index.jsp\n\nISGN, Indian Seismic and GNSS Network\nhttp://www.isgn.gov.in/ISGN/\n\nRSMC, Regional Specialized Meteorological Centre New Delhi\nhttp://www.rsmcnewdelhi.imd.gov.in/index.php?lang=en");
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
