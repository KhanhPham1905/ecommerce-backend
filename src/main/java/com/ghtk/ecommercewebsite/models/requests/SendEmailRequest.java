package com.ghtk.ecommercewebsite.models.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    Recipient to;
    String subject;
    String htmlContent;
}
