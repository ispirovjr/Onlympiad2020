package today.vfu.hospitals;

public class TypeIDReturn {


    static String getStringType(int typ) {

        String type = "";
        switch (typ) {
            case 1:
                type = "Covid Specific";
                break;
            case 2:
                type = "Emergency";
                break;
            case 3:
                type = "Converted";
                break;
            case 0:
            default:
                type = "Empty";
                break;
        }
        return type;
    }

    static int getIntType(String typ) {

        int type = 0;
        switch (typ) {
            case "Covid Specific":
                type = 1;
                break;
            case "Emergency":
                type = 2;
                break;
            case "Converted":
                type = 3;
                break;
        }
        return type;
    }


}
