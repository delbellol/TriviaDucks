package com.unimib.triviaducks.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

// Classe di utilità per verificare la disponibilità di Internet
public class NetworkUtil {

    // Metodo statico per verificare se è disponibile una connessione Internet
    public static boolean isInternetAvailable(Context context) {
        // Ottiene il ConnectivityManager per gestire la connettività di rete
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Verifica se il ConnectivityManager non è nullo
        if (connectivityManager != null) {
            // Ottiene la rete attiva corrente
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false; // Se non esiste una rete attiva, ritorna false

            // Ottiene le capacità della rete corrente
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities == null) return false; // Se non ha capacità definite, ritorna false

            // Verifica se la rete supporta trasporto WiFi, cellulare o Ethernet
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        }
        // Se ConnectivityManager è nullo, ritorna false
        return false;
    }
}
