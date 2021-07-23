package com.stemsence.paymentapp.utils;

import static com.stemsence.paymentapp.constants.constants.*;

public class DisbursementUtil {
    private final int teacherAmount;
    private final int stemAmount;
    private final int dutyAmount;
    private final int elcAmount;
    public DisbursementUtil(int amount){
        teacherAmount= amount * (TEACHER_SHARE/100);
        stemAmount = amount * (STEM_SHARE/100);
        elcAmount = amount * (ELC_SHARE/100);
        dutyAmount = amount * (DUTY/100);
    }

    public int getTeacherAmount() {
        return teacherAmount;
    }

    public int getStemAmount() {
        return stemAmount;
    }

    public int getDutyAmount() {
        return dutyAmount;
    }

    public int getElcAmount() {
        return elcAmount;
    }
}
