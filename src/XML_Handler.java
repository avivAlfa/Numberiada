import generated.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class XML_Handler {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";

    public static GameDescriptor getGameDescriptor(String xml_path) throws Exception{
        GameDescriptor gameDescriptor = null;
        InputStream inputStream;//XML_Handler.class.getResourceAsStream("\\resources\\random.xml");
        try {
            inputStream = new FileInputStream(xml_path);
             gameDescriptor = deserializeFrom(inputStream);


        } catch (JAXBException e) {
            throw e;
            // e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw e;
            //System.out.println("File not found!");
        }

        return gameDescriptor;
    }

    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }

    public static boolean validate(GameDescriptor gd)
    {
        return true;
    }





}
