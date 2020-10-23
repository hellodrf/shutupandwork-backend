package com.cervidae.shutupandwork.pojo;

import lombok.Data;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Base64;

@Data
public class AuthToken {

    @Pattern(regexp = "([A-Za-z0-9_]*)")
    @NotNull
    public String u;

    @Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")
    @NotNull
    public String p;

    public String getDecodedP() {
        String decoded = new String(Base64.getDecoder().decode(getP()));
        Assert.isTrue(decoded.matches("([A-Za-z0-9_.!?-]*)"), "3004");
        return decoded;
    }
}
