package Modal;

import java.io.FileInputStream;
import java.util.Properties;

public class SystemProperty {
	public Properties properties = null;

	public SystemProperty() {
		properties = new Properties();

		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\resources\\config.properties")) {

			properties.load(fis);
		} catch (Exception e) {
			System.out.println("Exception while reading config.properties");
			e.printStackTrace();
		}

	}

}
