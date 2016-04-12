import java.util.Formatter;

import by.gsu.epamlab.LeastFrequentlyUsed;

public class Runner {
	private static final String PUT = "put(%s)";
	private static final String GET = "get(%s)";
	private static LeastFrequentlyUsed lfuCache;
	
	public static void main(String[] args) {
		final int CACHE_SIZE = 4;
		final double ENVICTION_FACTOR = 0.9; 
		lfuCache = new LeastFrequentlyUsed(CACHE_SIZE, ENVICTION_FACTOR);
		
		final String ITEM_A = "a";
		final String ITEM_B = "b";
		final String ITEM_C = "c";
		final String ITEM_D = "d";
		
		System.out.println(printStep(PUT, 1, ITEM_A, ITEM_B, ITEM_C, ITEM_D));
		lfuCache.putItem(ITEM_A, 1);
		lfuCache.putItem(ITEM_B, 2);
		lfuCache.putItem(ITEM_C, 3);
		lfuCache.putItem(ITEM_D, 4);
		System.out.println(lfuCache);
		
		System.out.println(printStep(GET, 2, ITEM_C));
		getValue(ITEM_C);
		System.out.println(lfuCache);
		
		System.out.println(printStep(GET, 3, ITEM_D));
		getValue(ITEM_D);
		System.out.println(lfuCache);
		
		System.out.println(printStep(GET, 4, ITEM_C));
		getValue(ITEM_C);
		System.out.println(lfuCache);
		
		System.out.println(printStep(GET, 5, ITEM_C, ITEM_C, 
				ITEM_D, ITEM_D, ITEM_B, ITEM_B, ITEM_A, ITEM_A));
		getValue(ITEM_C);
		getValue(ITEM_C);
		getValue(ITEM_D);
		getValue(ITEM_D);
		getValue(ITEM_B);
		getValue(ITEM_B);
		getValue(ITEM_A);
		getValue(ITEM_A);
		System.out.println(lfuCache);
		
		System.out.println(printStep(GET, 6, ITEM_D, ITEM_C));
		getValue(ITEM_D);
		getValue(ITEM_C);
		System.out.println(lfuCache);
		
		final String ITEM_E = "e";
		System.out.println(printStep(PUT, 7, ITEM_E));
		lfuCache.putItem(ITEM_E, 5);
		System.out.println(lfuCache);
	}

	private static void getValue(String item) {
		final String NOT_FOUND_KEY = "The key \"%s\" is not found.";
		try {
			lfuCache.getValue(item);
		} catch (NullPointerException e) {
			System.out.printf(NOT_FOUND_KEY, item);
			System.out.println();
		}
	}

	private static String printStep(String step, int numberStep, String... items) {
		final String ARROW = "->";
		final String RESULT = "\nresult:";
		if(items != null && items.length > 0) {
			StringBuilder result = new StringBuilder();
			printStep(result, numberStep);
			for(int numItem = 0; numItem < items.length; numItem++) {
				if(items[numItem] != null && !items[numItem].isEmpty()) {
					
					@SuppressWarnings("resource")
					Formatter formatter = new Formatter();
					formatter.format(step, items[numItem]);
					result.append(formatter.toString());
					if(numItem < items.length - 1) {
						result.append(ARROW);
					}
				}
			}
			result.append(RESULT);
			return result.toString();
		}
		
		return "";
	}

	private static void printStep(StringBuilder result, int step) {
		final String STEP = "Step %d: ";
		
		@SuppressWarnings("resource")
		Formatter formatter = new Formatter();
		formatter.format(STEP, step);
		result.append(formatter.toString());
	}
}
