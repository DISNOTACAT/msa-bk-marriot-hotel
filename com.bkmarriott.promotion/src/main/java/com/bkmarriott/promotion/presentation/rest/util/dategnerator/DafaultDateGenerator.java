package com.bkmarriott.promotion.presentation.rest.util.dategnerator;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class DafaultDateGenerator implements DateGenerator {

    @Override
    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
