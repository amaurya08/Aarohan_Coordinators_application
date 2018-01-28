package org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo;

public class RegistrationDataPojo {
    private String stuName, stuId;

    public RegistrationDataPojo(String stuName, String stuId) {
        this.stuName = stuName;
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public String getStuId() {
        return stuId;
    }


    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }


    @Override
    public String toString() {
        return "RegistrationDataPojo{" +
                "stuName='" + stuName + '\'' +
                "stuId='" + stuId + '\'' +
                '}';
    }
}
