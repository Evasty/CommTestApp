package com.example.commtestapp;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.EditText;

public class DiscoveryHandler  {

    private NsdManager mNsdMan;
    private NsdManager.DiscoveryListener    mDiscoListen;
    private NsdManager.ResolveListener      mResolListen;
    private callback MTV;

    public static final String SERVICE_TYPE = "_services._dns-sd._udp";//_http._udp";//"_services._dns-sd._udp";//
    public static final String TAG = "NsdH H H H ";
    public static String mServiceName = "NsdChat";
    private Context mContext;

    public interface callback{
        public void append(String str);
    }

    public static DiscoveryHandler newInstance(Context context, callback mtv) {

        DiscoveryHandler DC = new DiscoveryHandler();
        DC.mNsdMan = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        DC.mContext = context;
        DC.mResolListen = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                Log.d(TAG, "onServiceResolved: ????");//mService = serviceInfo;
            }
        };

        DC.MTV = mtv;
        if(mtv == null){
            Log.d(TAG, "newInstance: fuckity");
        }
        return DC;
    }


    public void discoverServices() {
        stopDiscovery();  // Cancel any existing discovery request
        initializeDiscoveryListener();
        mNsdMan.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoListen);
    }

    public void stopDiscovery() {
        if (mDiscoListen != null) {
            try {
                mNsdMan.stopServiceDiscovery(mDiscoListen);
            } finally {
            }
            mDiscoListen = null;
        }
    }

    public void initializeDiscoveryListener() {
        mDiscoListen = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                MTV.append("\nnds::start ");
                Log.d(TAG, "Service discovery started");
            }
            @Override
            public void onServiceFound(NsdServiceInfo service) {
                MTV.append("\nsv:: "+service);
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (service.getServiceName().contains(mServiceName)){
                    Log.d(TAG, "onServiceFound: "+ mServiceName);
                    mNsdMan.resolveService(service, mResolListen);
                }
            }
            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                /*if (mService == service) {
                    mService = null;
                }*/
            }
            @Override
            public void onDiscoveryStopped(String serviceType) {
                MTV.append("\nnds::stop ");
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }
        };
    }

}
