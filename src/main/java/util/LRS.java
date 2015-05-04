package util;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-05-04 09:38.
 * Package: util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * * Linkable Ring Signature 
 */
public class LRS {
    private final static BigInteger prime = ElGamal.prime1024;
    private final static BigInteger g = ElGamal.generator1024;
   
    public static LRSRet sign(String m, List<BigInteger> publicKeys, BigInteger privateKey, Integer identity, BigInteger q) {
        
        try {
            String publicKeysStr = "";
            int n = publicKeys.size();
            for(int i = 0; i < publicKeys.size(); i++) {
                publicKeysStr += publicKeys.get(i).toString();
            }
            BigInteger h = hash2(publicKeysStr);
            

            BigInteger yPrime = h.modPow(privateKey,prime);

            Random rng = new SecureRandom();
            BigInteger u = new BigInteger(q.bitLength()-1, rng);
            
            
            String newMsg = "";
   
            newMsg += publicKeysStr;
            newMsg += yPrime.toString();
            newMsg += m.toString();
            newMsg += g.modPow(u,prime).toString();
            newMsg += h.modPow(u,prime).toString();

            BigInteger b = hash1(newMsg);
            
            BigInteger[] ci = new BigInteger[n];
            BigInteger[] si = new BigInteger[n];
            ci[(identity+1)%n] = b;
            
            for(int i = (identity+2)%n; i != identity+1; i = (i+1)%n) {
                int j = (i-1+n) % n;
                si[j] = new BigInteger(q.bitLength() - 1, rng);
                

                String temp = publicKeysStr + yPrime.toString() + m +
                        g.modPow(si[j],prime).multiply(publicKeys.get(j).modPow(ci[j],prime)).mod(prime) +
                        h.modPow(si[j],prime).multiply(yPrime.modPow(ci[j],prime)).mod(prime);
                ci[i] = hash1(temp);

            }

            si[identity] = u.subtract(privateKey.multiply(ci[identity])).mod(q);
            return new LRSRet(ci[0],si,yPrime);
        }catch (NoSuchAlgorithmException e) {
            System.out.println("Error in config hash algorithm in LRS.");
        }catch (UnsupportedEncodingException e) {
            System.out.println("Fails to encode in UTF8");
        }
        return null;
    }

    /**
     * * verify the signature
     * @return true if passing the verification
     */
    public static boolean verify(List<BigInteger> publicKeys, LRSRet lrs, String m) {
        try {           
            String publicKeysStr = "";
            int n = publicKeys.size();
            for(int i = 0; i < publicKeys.size(); i++) {
                publicKeysStr += publicKeys.get(i).toString();
            }


            BigInteger h = hash2(publicKeysStr);

            // zi and zi'
            BigInteger zi, zi_dash;
            BigInteger ci = lrs.c;

            for(int i = 0; i < n; i++) {
                zi = g.modPow(lrs.s[i], prime).multiply(
                        publicKeys.get(i).modPow(ci, prime)).mod(prime);

                zi_dash = h.modPow(lrs.s[i], prime).multiply(
                        lrs.yPrime.modPow(ci, prime)).mod(prime);
                String temp = publicKeysStr + lrs.yPrime.toString() + m + zi.toString() + zi_dash.toString();
                ci = hash1(temp);
            }

            return (lrs.c.equals(ci));
        }catch (Exception e) {
            System.out.println("Fails to verify!");
        }
        return false;
    }
    
    static class LRSRet {
        BigInteger c = null;
        BigInteger[] s = null;
        BigInteger yPrime = null;

        LRSRet(BigInteger c, BigInteger[] s, BigInteger yPrime) {
            this.c = c;
            this.s = s;
            this.yPrime = yPrime;
        }
    }
    
    public static BigInteger hash1(String str) throws  NoSuchAlgorithmException, UnsupportedEncodingException{
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            BigInteger h = new BigInteger(1, digest.digest(str.getBytes("UTF-8")));
            return h;

    }

    public static BigInteger hash2(String str) throws  NoSuchAlgorithmException, UnsupportedEncodingException {

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            BigInteger h = new BigInteger(1, digest.digest(str.getBytes("UTF-8")));
            h = h.mod(prime);
            h = g.modPow(h,prime);
            return h;

    }
}
