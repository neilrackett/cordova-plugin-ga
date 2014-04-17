package com.adobe.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.analytics.tracking.android.Transaction;

public class GAPlugin extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callback) {
		GoogleAnalytics ga = GoogleAnalytics.getInstance(cordova.getActivity());
		Tracker tracker = ga.getDefaultTracker(); 

		if (action.equals("initGA")) {
			try {
				tracker = ga.getTracker(args.getString(0));
				GAServiceManager.getInstance().setDispatchPeriod(args.getInt(1));
				ga.setDefaultTracker(tracker);
				callback.success("initGA - id = " + args.getString(0) + "; interval = " + args.getInt(1) + " seconds");
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
		} else if (action.equals("exitGA")) {
			try {
				GAServiceManager.getInstance().dispatch();
				callback.success("exitGA");
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
		} else if (action.equals("trackEvent")) {
			try {
				tracker.sendEvent(args.getString(0), args.getString(1), args.getString(2), args.getLong(3));
				callback.success("trackEvent - category = " + args.getString(0) + "; action = " + args.getString(1) + "; label = " + args.getString(2) + "; value = " + args.getInt(3));
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
		} else if (action.equals("trackPage")) {
			try {
				tracker.sendView(args.getString(0));
				callback.success("trackPage - url = " + args.getString(0));
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
			
		/*
		 * Track e-commerce transactions
		 * args = [transactionID, affiliation, revenue, tax, shipping, currencyCode]
		 * 
		 * @author Neil Rackett
		 */
		}
		else if (action.equals("trackTransaction")) 
		{
            try 
            {
            	Transaction trans = new Transaction.Builder(
            		args.getString(0),											// (String) Transaction Id, should be unique.
            	    (long) args.getDouble(2)*1000000)							// (long) Order total (in micros)
            	    .setAffiliation(args.getString(1))							// (String) Affiliation
            	    .setTotalTaxInMicros((long) args.getDouble(3)*1000000)		// (long) Total tax (in micros)
            	    .setShippingCostInMicros((long) args.getDouble(4)*1000000)	// (long) Total shipping cost (in micros)
            	    .setCurrencyCode(args.getString(5))							// (String) Currency code
            	    .build();
            	
                tracker.sendTransaction(trans);
                
                callback.success
                (
                    "trackEcommerceTransaction - Transaction ID = "+ args.getString(0) +
                    " Affiliation "+args.getString(1) +
                    " Revenue " + args.getDouble(2)+
                    " Tax " +   args.getDouble(3)+
                    " Shipping " + args.getDouble(4)+
                    " Currency code " + args.getString(5)
                );
                
                return true;
                
            } catch(final Exception e){
                callback.error(e.getMessage());
            }
            
		} else if (action.equals("setVariable")) {
			try {
				tracker.setCustomDimension(args.getInt(0), args.getString(1));
				callback.success("setVariable passed - index = " + args.getInt(0) + "; value = " + args.getString(1));
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
		}
		else if (action.equals("setDimension")) {
			try {
				tracker.setCustomDimension(args.getInt(0), args.getString(1));
				callback.success("setDimension passed - index = " + args.getInt(0) + "; value = " + args.getString(1));
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
		}
		else if (action.equals("setMetric")) {
			try {
				tracker.setCustomMetric(args.getInt(0), args.getLong(1));
				callback.success("setVariable passed - index = " + args.getInt(2) + "; key = " + args.getString(0) + "; value = " + args.getString(1));
				return true;
			} catch (final Exception e) {
				callback.error(e.getMessage());
			}
		}
		
		return false;
	}
}

