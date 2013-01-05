package se.acrend.sj2cal.preference;

/**
 * Interface used to be able to mock SharedPreference.
 *
 */
public interface PreferencesInstance {

	boolean isParseSj();

	boolean isParseSwebus();
	
	boolean isParseOresund();

}
