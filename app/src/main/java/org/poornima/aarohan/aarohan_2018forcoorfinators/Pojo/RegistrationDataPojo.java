package org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo;

/**
 * Created by Bhoomika on 13-01-2018.
 */

public class RegistrationDataPojo {
    private String stuName,stuId;

    public RegistrationDataPojo(String stuName, String stuId ) {
        this.stuName = stuName;
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public String getStuId() {
        return stuId;
    }
}
