package util;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-05-04 10:52.
 * Package: util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class LRSTest {
    public final BigInteger p = ElGamal.prime1024;
    public final BigInteger g = ElGamal.generator1024;
    
    @Test
    public void lrs() {
        // test regular case
        try {
            int n = 4;
            int identity = 2;
            BigInteger q = ElGamal.generateQ(p);

            List<BigInteger> publicKeys = new ArrayList<BigInteger>();
            // generate data
            Random rng = new SecureRandom();

            for(int i = 0; i < n; i++) {
                BigInteger privateTmp = new BigInteger(q.bitLength()-1,rng);
                publicKeys.add(g.modPow(privateTmp,p));
            }

            //generate private/public key pair
            BigInteger privateKey = new BigInteger(q.bitLength()-1,rng);
            BigInteger publicKeySelf = g.modPow(privateKey,p);
            publicKeys.set(identity,publicKeySelf);

            String m ="weak";
            LRS.LRSRet ret = LRS.sign(m, publicKeys, privateKey, identity, q);
            assertTrue(LRS.verify(publicKeys,ret,m));
        } catch (Exception e) {
            fail();
        }

    }
}
