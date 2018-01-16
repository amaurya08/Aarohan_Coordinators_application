package org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo;

/**
 * Created by ADMIN on 15-Jan-18.
 */

public class EventStudentPojo {
    private String stu_name,stu_reg_no,stu_college,stu_email,stu_contact,ev_event_att;

    public EventStudentPojo(String stu_name, String stu_reg_no, String stu_college, String stu_email, String stu_contact,String ev_event_att) {
        this.stu_name = stu_name;
        this.stu_reg_no = stu_reg_no;
        this.stu_college = stu_college;
        this.stu_email = stu_email;
        this.stu_contact = stu_contact;
        this.ev_event_att = ev_event_att;
    }

    public String getStu_name() {
        return stu_name;
    }

    public String getStu_reg_no() {
        return stu_reg_no;
    }

    public String getStu_college() {
        return stu_college;
    }

    public String getStu_email() {
        return stu_email;
    }

    public String getStu_contact() {
        return stu_contact;
    }
    public String getEv_event_att() {
        return ev_event_att;
    }
}
