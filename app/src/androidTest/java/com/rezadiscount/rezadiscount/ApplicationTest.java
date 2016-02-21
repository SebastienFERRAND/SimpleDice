package com.rezadiscount.rezadiscount;

import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

import com.rezadiscount.rezadiscount.reza.discount.Grenadeal;

public class ApplicationTest extends ApplicationTestCase<Grenadeal> {

    private Grenadeal application;

    public ApplicationTest() {
        super(Grenadeal.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();

    }

    public void testCorrectVersion() throws Exception {
        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        assertNotNull(info);
        MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName);
    }

}
