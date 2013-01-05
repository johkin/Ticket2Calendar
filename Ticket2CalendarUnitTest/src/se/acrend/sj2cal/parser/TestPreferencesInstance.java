package se.acrend.sj2cal.parser;

import se.acrend.sj2cal.preference.PreferencesInstance;

public class TestPreferencesInstance implements PreferencesInstance {

	@Override
	public boolean isParseSj() {
		return true;
	}

	@Override
	public boolean isParseSwebus() {
		return true;
	}

	@Override
	public boolean isParseOresund() {
		return true;
	}
}
