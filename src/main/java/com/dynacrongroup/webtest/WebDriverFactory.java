package com.dynacrongroup.webtest;

import java.util.ArrayList;
import java.util.List;

import com.dynacrongroup.webtest.util.ConfigurationValue;

/**
 * This class figures out which WebDriver(s) to set up.
 */
public class WebDriverFactory {

    public final static String WEBDRIVER_DRIVER = "WEBDRIVER_DRIVER";
    public final static String SINGLE_SAUCE = "SINGLE_SAUCE";
    public final static String DEFAULT_TARGETS = "DEFAULT_TARGETS";

    public List<String[]> pair(String key, String value) {
        List<String[]> results = new ArrayList<String[]>();
        results.add(new String[]{key, value});
        return results;
    }

    public List<String[]> getDriverTargets() {

        String selected = ConfigurationValue.getConfigurationValue(
                WEBDRIVER_DRIVER, null);
        if (selected != null) {
            return pair("byclass", selected);
        }

        String single_sauce = ConfigurationValue.getConfigurationValue(
                SINGLE_SAUCE, null);
        if (single_sauce != null) {
            String[] items = splitTarget(single_sauce);
            return pair(items[0], items[1]);
        }

        return standardSauceLabsTargets();
    }

    /**
     * These are the standard SauceLabs targets for running a multi-browser test.
     * They can be configured using the DEFAULT_TARGETS config value.
     *
     * @return A list of string arrays; each list element is a paired browser/version.
     */
    private List<String[]> standardSauceLabsTargets() {
        List<String[]> result = new ArrayList<String[]>();

        String[] defaultSauceLabsTargets = ConfigurationValue.getConfigurationValue(DEFAULT_TARGETS,
                "firefox:5,iexplore:7,iexplore:8,iexplore:9,chrome:*").split(",");

        for (String windowsBrowser : defaultSauceLabsTargets) {
            result.add(splitTarget(windowsBrowser));
        }

        return result;
    }

    /**
     * Splits a target string into browser/version String pair, with version set
     * to null if version is "*".
     *
     * @param target a browser:version string (iexplore:8, for example)
     * @return target split into separate strings for browser and version.
     */
    private String[] splitTarget(String target) {
        String[] items = target.split(":");
        items[1] = items[1].replaceAll("\\*", "");  //* used to refer to "any browser".  Now null is used.
        return new String[]{items[0], items[1]};
    }
}
