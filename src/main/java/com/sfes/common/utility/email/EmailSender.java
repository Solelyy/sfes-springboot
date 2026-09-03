package com.sfes.common.utility.email;

import com.sfes.common.classes.EmailContext;

public interface EmailSender {
    void sendEmail(EmailContext emailContext);
}
