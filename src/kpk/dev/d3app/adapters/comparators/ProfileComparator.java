package kpk.dev.d3app.adapters.comparators;

import java.util.Comparator;

import kpk.dev.d3app.models.accountmodels.ProfileModel;

public class ProfileComparator implements Comparator<ProfileModel> {
	@Override
	public int compare(ProfileModel lhs, ProfileModel rhs) {
		if(lhs.getLastUpdated() < rhs.getLastUpdated()) {
			return -1;
		}else if(lhs.getLastUpdated() == rhs.getLastUpdated()){
			return 0;
		}else{
			return 1;
		}
	}
}
