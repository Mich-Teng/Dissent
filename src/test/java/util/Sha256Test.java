package util;

import org.junit.Test;

import java.security.MessageDigest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-28 11:59.
 * Package: util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Sha256Test {
    @Test
    public void sha256() {
        // test regular case
        try {
            String text = "hello";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            assertEquals(sb.toString(), "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
            //System.out.println(new BigInteger(1,hash));
        } catch (Exception e) {
            fail();
        }

    }
}
