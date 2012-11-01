package com.py110.theme;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ThemeProvider;
import org.zkoss.zul.Messagebox;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;


public class CacheableThemeProvider implements ThemeProvider {
	private final static String BLUE_MESSAGEBOX_TEMPLATE_URI = "~./zul/html/messagebox.zul";
	private final static String BREEZE_MESSAGEBOX_TEMPLATE_URI = "~./zul/html/messagebox.breeze.zul";

	public Collection getThemeURIs(Execution exec, List uris) {
		String theme = Themes.getThemeStyle(exec);
		if(Strings.isEmpty(theme) || Strings.isBlank(theme))
			Themes.setThemeStyle(exec, Themes.BREEZE_THEME);

		boolean isBreeze = Themes.isBreeze(exec) && Themes.hasBreezeLib();
		Messagebox.setTemplate(isBreeze ?
                BREEZE_MESSAGEBOX_TEMPLATE_URI : BLUE_MESSAGEBOX_TEMPLATE_URI);
		/* Use Breeze Theme as default theme*/
		if (isBreeze) {
			proessBreezeURI(uris);
			return uris;
		}

		String fsc = Themes.getFontSizeCookie(exec);
		boolean isSilvergray = Themes.isSilvergray(exec) && Themes.hasSilvergrayLib();
		processSilverAndFontURI(isSilvergray, uris, fsc);

		//slivergray
		if (isSilvergray) {
			uris.add("~./silvergray/color.css.dsp");
			uris.add("~./silvergray/img.css.dsp");
		}

		if ("lg".equals(fsc)) {
			uris.add("/css/fontlg.css.dsp");
			if (isSilvergray)
				uris.add("/css/silvergraylg.css.dsp");
		} else if ("sm".equals(fsc)) {
			uris.add("/css/fontsm.css.dsp");
			if (isSilvergray)
				uris.add("/css/silvergraysm.css.dsp");
		}

		return uris;
	}

	/**
	 * Setup inject URI for cache and removes silvergray URI
	 * @param uris
	 */
	private static void proessBreezeURI (List uris) {
		for (ListIterator it = uris.listIterator(); it.hasNext();) {
			final String uri = (String)it.next();

			if (uri.startsWith(Themes.DEFAULT_WCS_URI)) {
				it.set(Aide.injectURI(uri, Themes.BREEZE_THEME));
				continue;
			} else if (uri.startsWith(Themes.DEFAULT_SILVERGRAY_URI))
				it.remove();
		}
	}

	/**
	 * Setup inject URI for font and silvergray
	 * @param isSilver
	 * @param uris
	 * @param fsn
	 */
	private static void processSilverAndFontURI (boolean isSilver, List uris, String fsn) {
		for (ListIterator it = uris.listIterator(); it.hasNext();) {
			final String uri = (String)it.next();
			if (isSilver) {
				if (uri.startsWith(Themes.DEFAULT_WCS_URI)) {
					String injectURI = Themes.SILVERGRAY_THEME + ((fsn != null && fsn.length() > 0) ? "-" + fsn : "");
					it.set(Aide.injectURI(uri, injectURI));
				}
			} else {
				/*Remove silvergray URI*/
				if (uri.startsWith(Themes.DEFAULT_SILVERGRAY_URI))
					it.remove();
				else if (fsn != null && fsn.length() > 0 && uri.startsWith(Themes.DEFAULT_WCS_URI))
					it.set(Aide.injectURI(uri, fsn));
			}
		}
	}

	public int getWCSCacheControl(Execution exec, String uri) {
		return 8760; //safe to cache
	}
	public String beforeWCS(Execution exec, String uri) {
		final String[] dec = Aide.decodeURI(uri);
		if (dec != null) {
			if ("lg".equals(dec[1])) {
				exec.setAttribute("fontSizeM", "15px");
				exec.setAttribute("fontSizeMS", "13px");
				exec.setAttribute("fontSizeS", "13px");
				exec.setAttribute("fontSizeXS", "12px");
			} else if ("sm".equals(dec[1])) {
				exec.setAttribute("fontSizeM", "10px");
				exec.setAttribute("fontSizeMS", "9px");
				exec.setAttribute("fontSizeS", "9px");
				exec.setAttribute("fontSizeXS", "8px");
			}
			return dec[0];
		}
		return uri;
	}

	public String beforeWidgetCSS(Execution exec, String uri) {
		if (!Themes.hasBreezeLib() || !Themes.isBreeze(exec))
			return uri;
		if(uri.startsWith("~./js/zul/") || uri.startsWith("~./js/zkex/")
				|| uri.startsWith("~./js/zkmax/") || uri.startsWith("~./zul/css/"))
			return uri.replaceFirst(".css.dsp", ".breeze.css.dsp");
		return uri;
	}
}
