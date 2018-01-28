package org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo;

/**
 * Created by ADMIN on 19-Jan-18.
 */

public class AccommodationStudentPojo {
    private String stu_name, stu_reg_no, room, rs_payment_status, rc_check_in, rc_check_out;

    public AccommodationStudentPojo(String stu_name, String stu_reg_no, String room, String rs_payment_status, String rc_check_in, String rc_check_out) {
        this.stu_name = stu_name;
        this.stu_reg_no = stu_reg_no;
        this.room = room;
        this.rs_payment_status = rs_payment_status;
        this.rc_check_in = rc_check_in;
        this.rc_check_out = rc_check_out;
    }

    public String getStu_name() {
        return stu_name;
    }

    public String getStu_reg_no() {
        return stu_reg_no;
    }

    public String getRoom() {
        return room;
    }

    public String getRs_payment_status() {
        return rs_payment_status;
    }

    public String getRc_check_in() {
        return rc_check_in;
    }

    public String getRc_check_out() {
        return rc_check_out;
    }
}
