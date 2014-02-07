package game;
public class Vector {
	public static double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
	}
	public static double angle(double x1, double y1, double x2, double y2){
		if(x1 < x2){
			return 90 + Math.toDegrees(Math.atan((y2-y1)/(x2-x1)));
		}
		else if(x1 == x2){
			if(y1 > y2){
				return 0;
			}
			else{
				return 180;
			}
		}
		else{
			return 270 - Math.toDegrees(Math.atan(-(y2-y1)/(x2-x1)));
		}
	}
	public static double xComponent(double angleDeg, double magnitude){
		return (Math.sin(Math.toRadians(angleDeg))*magnitude);
	}
	public static double yComponent(double angleDeg, double magnitude){
		return (Math.cos(Math.toRadians(angleDeg))*magnitude);
	}
	public static double modulus(double number){
		if(number >= 0){
			return number;
		}
		else{
			return -number;
		}
	}
}
