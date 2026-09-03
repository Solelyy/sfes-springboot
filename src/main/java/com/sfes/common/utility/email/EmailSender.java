package com.sfes.superadmin.account.email;

import com.sfes.common.classes.EmailContext;

public interface EmailSender {
    void sendEmail(EmailContext emailContext);
}
