package com.bfsi.egalite.util;

import android.speech.tts.TextToSpeech;

public  class  NumberToWords
 {

	private static double getPlace(String number) {
		switch (number.length()) {
		case 1:
			return DefinePlace.UNITS;
		case 2:
			return DefinePlace.TENS;
		case 3:
			return DefinePlace.HUNDREDS;
		case 4:
			return DefinePlace.THOUSANDS;
		case 5:
			return DefinePlace.TENTHOUSANDS;
		case 6:
			return DefinePlace.MILLIONS;
		case 7:
			return DefinePlace.TENMILLIONS;
		case 8:
			return DefinePlace.BILLIONS;
		case 9:
			return DefinePlace.TENBILLIONS;
		default:
			return 0;
		}

	}

	private static String getWord(int number) {
		switch (number) {
		case 1:
			return "One";
		case 2:
			return "Two";
		case 3:
			return "Three";
		case 4:
			return "Four";
		case 5:
			return "Five";
		case 6:
			return "Six";
		case 7:
			return "Seven";
		case 8:
			return "Eight";
		case 9:
			return "Nine";
		case 0:
			return "Zero";
		case 10:
			return "Ten";
		case 11:
			return "Eleven";
		case 12:
			return "Twelve";
		case 13:
			return "Thirteen";
		case 14:
			return "Forteen";
		case 15:
			return "Fifteen";
		case 16:
			return "Sixteen";
		case 17:
			return "Seventeen";
		case 18:
			return "Eighteen";
		case 19:
			return "Ninteen";
		case 20:
			return "Twenty";
		case 30:
			return "Thirty";
		case 40:
			return "Forty";
		case 50:
			return "Fifty";
		case 60:
			return "Sixty";
		case 70:
			return "Seventy";
		case 80:
			return "Eighty";
		case 90:
			return "Ninty";
		case 100:
			return "Hundred";
		default:return "";
		}
	}

	private static String cleanNumber(String number) {
		String cleanedNumber = "";
		cleanedNumber = number.replace('.', ' ').replaceAll(" ", "");
		cleanedNumber = cleanedNumber.replace(',', ' ').replaceAll(" ", "");
		if (cleanedNumber.startsWith("0"))
			cleanedNumber = cleanedNumber.replaceFirst("0", "");

		return cleanedNumber;
	}

	public static String convertNumber(String number) {
		number = cleanNumber(number);
		double num = 0.0;
		try {
			num = Double.parseDouble(number);
		} catch (Exception e) {
			return "Invalid Number";
		} // catch

		String returnValue = "";
		while (num > 0) {
			number = "" + (int) num;
			double place = getPlace(number);
			if (place == DefinePlace.TENS || place == DefinePlace.TENTHOUSANDS
					|| place == DefinePlace.TENMILLIONS
					|| place == DefinePlace.TENBILLIONS) {
				int subNum = Integer.parseInt(number.charAt(0) + ""
						+ number.charAt(1));
				if (subNum >= 21 && (subNum % 10) != 0) {
					returnValue += getWord(Integer.parseInt(""
							+ number.charAt(0)) * 10)
							+ " " + getWord(subNum % 10);
				} // if
				else {
					returnValue += getWord(subNum);
				}// else

				if (place == DefinePlace.TENS) {
					num = 0;
				}// if
				else if (place == DefinePlace.TENTHOUSANDS) {
					num -= subNum * DefinePlace.THOUSANDS;
					returnValue += " Thousand ";

				}// if
				else if (place == DefinePlace.TENMILLIONS) {
					num -= subNum * DefinePlace.MILLIONS;
					returnValue += " Million ";

				}// if
				else if (place == DefinePlace.TENBILLIONS) {
					num -= subNum * DefinePlace.BILLIONS;
					returnValue += " Billion ";

				}// if

			} else {
				int subNum = Integer.parseInt("" + number.charAt(0));
				returnValue += getWord(subNum);
				if (place == DefinePlace.UNITS) {
					num = 0;
				}// if
				else if (place == DefinePlace.HUNDREDS) {
					num -= subNum * DefinePlace.HUNDREDS;
					returnValue += " Hundred ";
				}// if
				else if (place == DefinePlace.THOUSANDS) {
					num -= subNum * DefinePlace.THOUSANDS;
					returnValue += " Thousand ";
				}// if
				else if (place == DefinePlace.MILLIONS) {
					num -= subNum * DefinePlace.MILLIONS;
					returnValue += " Million ";
				}// if
				else if (place == DefinePlace.BILLIONS) {
					num -= subNum * DefinePlace.BILLIONS;
					returnValue += " Billion ";
				}// if
			}// else
		}// while
		return returnValue;
	}// convert number
	
	
	public static void speakText(String mToSpeak,TextToSpeech mttsp){
		String outCome = "";String toSpeak="";
		if (mToSpeak != null
				&& !(mToSpeak.equals("")))
		{
			toSpeak= mToSpeak;
			String one = toSpeak.substring(0, toSpeak.indexOf("."));
			String two = toSpeak.substring(toSpeak.indexOf("."));
			
			if(convertNumber(two).equalsIgnoreCase("Invalid Number"))
				outCome = "Zero";
			else
				outCome = convertNumber(two);
			
			String speak = convertNumber(one) +" Dot "+ outCome;
			mttsp.speak(speak, TextToSpeech.QUEUE_FLUSH, null);

	   }
	}

}

	
