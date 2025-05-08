package com.mohasihab.cram.core.helper

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

object BiometricHelper {

    private const val ALIAS = "rsa_key"
    private const val KEYSTORE="AndroidKeyStore"
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("Biometric", "Authentication succeeded")
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.e("Biometric", "Error: $errString")
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.e("Biometric", "Authentication failed")
                    onError("Authentication failed")
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }

    fun generateRSAKeyPair(): PublicKey {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", KEYSTORE)
        val parameterSpec = KeyGenParameterSpec.Builder(
            ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .setUserAuthenticationRequired(false)
            .build()

        keyPairGenerator.initialize(parameterSpec)
        return keyPairGenerator.generateKeyPair().public
    }

    fun signDataWithRSA(data: ByteArray): ByteArray {
        try {
            val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
            val privateKey = keyStore.getKey(ALIAS, null) as PrivateKey
            val signature = Signature.getInstance("SHA256withRSA").apply {
                initSign(privateKey)
                update(data)
            }
            return signature.sign()
        }
        catch (ex : Exception){
            Log.d("CRAM","Exception in signDataWithRSA : ${ex.message}")
            return ByteArray(0)
        }

    }

    fun encodePublicKey(publicKey: PublicKey): String {
        return Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
    }

    fun deleteKey() {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
        if (keyStore.containsAlias(ALIAS)) {
            keyStore.deleteEntry(ALIAS)
            Log.d("Biometric", "Key with alias '$ALIAS' has been deleted.")
        } else {
            Log.d("Biometric", "No key with alias '$ALIAS' found.")
        }
    }
}