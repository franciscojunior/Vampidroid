package name.vampidroid;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

public class VampidroidSuggestionProvider extends
		SearchRecentSuggestionsProvider {
	
	private static String AUTH="name.vampidroid.VampidroidSuggestionProvider";
	
	static SearchRecentSuggestions getBridge(Context ctx)
	{
		return new SearchRecentSuggestions(ctx, AUTH, DATABASE_MODE_QUERIES);
		
	}

	public VampidroidSuggestionProvider() {
		super();
		// TODO Auto-generated constructor stub
		
		setupSuggestions(AUTH, DATABASE_MODE_QUERIES);
	}
	
	

}
