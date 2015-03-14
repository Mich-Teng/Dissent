package util;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;

import static org.junit.Assert.*;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-03-02 12:41.
 * Package: util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class TestEncryption {
    public static final int SERVER_NUM = 3;
    private ElGamal elGamal = new ElGamal();
    CommutativeElGamal[] ces = new CommutativeElGamal[SERVER_NUM];
    private BigInteger g = elGamal.getGenerator();
    private BigInteger p = elGamal.getPrime();

    @Before
    public void init() {
        for (int i = 0; i < SERVER_NUM; i++)
            ces[i] = new CommutativeElGamal();
    }

    @Test
    public void generateNym() {
        // generate nym
        for (int i = 0; i < SERVER_NUM; i++)
            g = g.modPow(ces[i].getPrivateKey(), p);
        BigInteger nym = g.modPow(elGamal.getPrivateKey(), p);
        BigInteger y = elGamal.getPublicKey();
        for (int i = 0; i < SERVER_NUM; i++)
            y = y.modPow(ces[i].getPrivateKey(), p);
        assertEquals(nym, y);
    }

    @Test
    public void signVerification() {
        try {
            BigInteger gTmp = g;
            BigInteger nym = elGamal.getPublicKey();
            for (int i = 0; i < SERVER_NUM; i++) {
                gTmp = gTmp.modPow(ces[i].getPrivateKey(), p);
                nym = nym.modPow(ces[i].getPrivateKey(), p);
            }
            assertEquals(gTmp.modPow(elGamal.getPrivateKey(), p), nym);

            String text = "hello";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            BigInteger[] signature = elGamal.sign(data, gTmp);
            assertTrue(ElGamal.verify(gTmp.modPow(elGamal.getPrivateKey(), p), data, signature[0], signature[1], gTmp, p));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSignBaseCase() {
        try {
            String text = "hello";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            //BigInteger p = new BigInteger("11");
            BigInteger x = elGamal.getPrivateKey();
            //BigInteger g = new BigInteger("2");
            BigInteger[] sign = elGamal.sign(p, g, x, data);
            assertTrue(ElGamal.verify(g.modPow(x, p), data, sign[0], sign[1], g, p));
        } catch (Exception e) {
            fail();
        }

    }


    @Test
    public void commutativeEncrypt() {
        BigInteger num = new BigInteger("0");
        // encrypt
        for (int i = 0; i < SERVER_NUM; i++)
            num = ces[i].encrypt(num)[2];
        // decrypt
        for (int i = SERVER_NUM - 1; i >= 0; i--)
            num = ces[i].decrypt(num);
        assertEquals(new BigInteger("0"), num);
    }
}
