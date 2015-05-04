package util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Reference: https://github.com/DeDiS/Surveillance/blob/master/src/psi/ElGamal.java
 */
public class ElGamal {

    private Random rng;
    private BigInteger p; // prime number
    private BigInteger g; // generator
    private BigInteger privateKey;
    private BigInteger publicKey;

    public static final BigInteger prime1024 = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80D"
            + "C1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE135"
            + "6D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE64"
            + "9286651ECE65381FFFFFFFFFFFFFFFF", 16);
    public static final BigInteger generator1024 = new BigInteger("2");

    public static final BigInteger prime768 = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC"
            + "1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A"
            + "6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A63A3620FFFFFFFFFFFFFFFF", 16);
    public static final BigInteger generator768 = new BigInteger("2");

    /**
     * Generates a new ElGamal object with preset group and generator, and randomly generated keys.
     */
    public ElGamal() {
        rng = new SecureRandom();
        p = prime1024;
        g = generator1024;
        privateKey = new BigInteger(p.bitLength() - 1, rng);
        publicKey = g.modPow(privateKey, p);
    }

    /**
     * Generates a new ElGamal object with selected group and generator, and randomly generated keys.
     *
     * @param p A prime number.
     * @param g A generator of the group Zp*.
     */
    public ElGamal(BigInteger p, BigInteger g) {
        rng = new SecureRandom();
        this.p = p;
        this.g = g;
        privateKey = new BigInteger(p.bitLength() - 1, rng);
        publicKey = g.modPow(privateKey, p);
    }

    /**
     * Generates a new ElGamal object with selected group, generator, and private key.
     *
     * @param p          A prime number.
     * @param g          A generator of the group Zp*.
     * @param privateKey A number < p/2.
     */
    public ElGamal(BigInteger p, BigInteger g, BigInteger privateKey) {
        rng = new SecureRandom();
        this.p = p;
        this.g = g;
        this.privateKey = privateKey;
        publicKey = g.modPow(privateKey, p);
    }

    /**
     * Generates a new ElGamal object with preset group and generator, and selected private key.
     *
     * @param privateKey A number < p/2.
     */
    public ElGamal(BigInteger privateKey) {
        rng = new SecureRandom();
        p = prime1024;
        g = generator1024;
        this.privateKey = privateKey;
        publicKey = g.modPow(privateKey, p);
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrime() {
        return p;
    }

    public BigInteger getGenerator() {
        return g;
    }

    public Random getRNG() {
        return rng;
    }

    /**
     * Encrypts data, using public key that belongs to the same group as this ElGamal object.
     *
     * @param data      BigInteger data to encrypt.
     * @param publicKey Public key.
     * @return Encrypted data (two bigIntegers)
     */
    public BigInteger[] encrypt(BigInteger publicKey, BigInteger data) {
        BigInteger[] c = new BigInteger[2];
        BigInteger y = new BigInteger(p.bitLength() - 1, rng);
        c[0] = g.modPow(y, p);
        c[1] = publicKey.modPow(y, p);
        c[1] = c[1].multiply(data);
        c[1] = c[1].mod(p);
        return c;
    }

    public BigInteger[] encrypt(BigInteger data, BigInteger a, BigInteger y) {
        BigInteger[] c = new BigInteger[2];
        c[0] = a;
        c[1] = publicKey.modPow(y, p);
        c[1] = c[1].multiply(data);
        c[1] = c[1].mod(p);
        return c;
    }

    /**
     * Encrypts data using this object's public key.
     *
     * @param data BigInteger data to encrypt.
     * @return Encrypted data (two bigIntegers)
     */
    public BigInteger[] encrypt(BigInteger data) {
        return encrypt(publicKey, data);
    }

    /**
     * Decrypts ciphertext using this object's private key.
     *
     * @param c The ciphertext to decrypt.
     * @return Length-1 array containing plaintext.
     */
    public BigInteger decrypt(BigInteger[] c) {
        BigInteger s = c[0].modPow(privateKey, p);
        s = s.modInverse(p);
        s = s.multiply(c[1]);
        return s.mod(p);
    }

    public BigInteger[] sign(BigInteger data, BigInteger g) {
        return sign(p, g, privateKey, data);
    }

    /**
     * * Using ElGmal signature to sign data 
     * @param p big prime number
     * @param g generator
     * @param privateKey private key
     * @param data data to be signed
     * @return signed key pair
     */
    public BigInteger[] sign(BigInteger p, BigInteger g, BigInteger privateKey, BigInteger data) {
        BigInteger[] ret = new BigInteger[2];
        BigInteger one = new BigInteger("1");
        // generate a random prime which is less than p
        /*
        BigInteger k = BigInteger.probablePrime(p.bitLength() - 1, rng);
        while (p.subtract(one).mod(k).equals(new BigInteger("0")))
            k = BigInteger.probablePrime(p.bitLength() - 1, rng);

        // gcd(k,p-1) = 1
        while (!k.gcd(p.subtract(one)).equals(one)) {
            k = new BigInteger(p.bitLength() - 1, rng);
        }
        */
        BigInteger k = ElGamal.generateQ(p);
  
        BigInteger dividend = p.subtract(one);
        ret[0] = g.modPow(k, p);
        // s = k-1(m− xr) mod p−1
        ret[1] = data.subtract(privateKey.multiply(ret[0]));
        ret[1] = ret[1].multiply(k.modInverse(dividend));
        ret[1] = ret[1].mod(dividend);
        return ret;
    }

    /**
     * * generate a prime number which is less than p 
     * @param p big prime number
     * @return q
     */
    public static BigInteger generateQ(BigInteger p) {
        Random rng = new SecureRandom();
        BigInteger one = new BigInteger("1");
        BigInteger k = BigInteger.probablePrime(p.bitLength() - 1, rng);
        while (p.subtract(one).mod(k).equals(new BigInteger("0")) || !k.gcd(p.subtract(one)).equals(one))
            k = BigInteger.probablePrime(p.bitLength() - 1, rng);
        return k;
    }

    /**
     * * verify the signature
     * @param publicKey public key
     * @param data signed data
     * @param r private key
     * @param s signature
     * @param g generator
     * @param p big prime number
     * @return true if verified
     */
    public static boolean verify(BigInteger publicKey, BigInteger data, BigInteger r, BigInteger s, BigInteger g, BigInteger p) {
        return g.modPow(data, p).equals((publicKey.modPow(r, p).multiply(r.modPow(s, p))).mod(p));
    }
}