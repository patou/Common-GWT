package com.sfeir.common.gwt.rebind.checker;

/*
 * ClassFinder.java
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

/**
 * This utility class was based originally on <a href="private.php?do=newpm&u=47838">Daniel Le Berre</a>'s <code>RTSI</code> class. This class can be called in different modes, but
 * the principal use is to determine what subclasses/implementations of a given class/interface exist in the current runtime environment.
 * 
 * @author Daniel Le Berre, Elliott Wade
 */
public class ClassFinder {
	private JClassType searchClass = null;
	private Map<URL, String> classpathLocations = new HashMap<URL, String>();
	private Map<String, URL> results = new HashMap<String, URL>();
	private List<Throwable> errors = new ArrayList<Throwable>();
	private boolean working = false;
	private final TypeOracle typeOracle;

	public ClassFinder(TypeOracle typeOracle) {
		this.typeOracle = typeOracle;
	}

	/**
	 * @param fqcn
	 *            Name of superclass/interface on which to search
	 */
	public final Vector<String> findNotCompileGWTclass(String packageSearch) {
		synchronized (classpathLocations) {
			synchronized (results) {
				try {
					working = true;
					searchClass = null;
					errors = new ArrayList<Throwable>();
					results = new TreeMap<String, URL>(CLASS_COMPARATOR);
					//
					// filter malformed FQCN
					//
					if (packageSearch.startsWith(".") || packageSearch.endsWith(".")) {
						return new Vector<String>();
					}

					return findNotCompileGWTclass(packageSearch, getClasspathLocations(packageSearch));
				} finally {
					working = false;
				}
			}
		}
	}

	public final List<Throwable> getErrors() {
		return new ArrayList<Throwable>(errors);
	}

	/**
	 * The result of the last search is cached in this object, along with the URL that corresponds to each class returned. This method may be called to query the cache for the
	 * location at which the given class was found. <code>null</code> will be returned if the given class was not found during the last search, or if the result cache has been
	 * cleared.
	 */
	public final URL getLocationOf(String clsName) {
		if (results != null)
			return results.get(clsName);
		else
			return null;
	}

	/**
	 * Determine every URL location defined by the current classpath, and it's associated package name.
	 */
	public final Map<URL, String> getClasspathLocations(String packageSearch) {
		Map<URL, String> map = new TreeMap<URL, String>(URL_COMPARATOR);
		File file = null;

		String pathSep = System.getProperty("path.separator");
		String classpath = System.getProperty("java.class.path");
		// System.out.println ("classpath=" + classpath);

		StringTokenizer st = new StringTokenizer(classpath, pathSep);
		while (st.hasMoreTokens()) {
			String path = st.nextToken();
			file = new File(path);
			include(null, file, map, packageSearch);
		}

//		Iterator<URL> it = map.keySet().iterator();
//		while (it.hasNext()) {
//			URL url = it.next();
//			System.out.println(url + "-->" + map.get(url));
//		}

		return map;
	}

	private final static FileFilter DIRECTORIES_ONLY = new FileFilter() {
		public boolean accept(File f) {
			if (f.exists() && f.isDirectory())
				return true;
			else
				return false;
		}
	};

	private final static Comparator<URL> URL_COMPARATOR = new Comparator<URL>() {
		public int compare(URL u1, URL u2) {
			return String.valueOf(u1).compareTo(String.valueOf(u2));
		}
	};

	private final static Comparator<String> CLASS_COMPARATOR = new Comparator<String>() {
		public int compare(String c1, String c2) {
			return String.valueOf(c1).compareTo(String.valueOf(c2));
		}
	};

	private final void include(String name, File file, Map<URL, String> map, String packageSearch) {
		if (!file.exists())
			return;
		if (!file.isDirectory()) {
			// could be a JAR file
			includeJar (file, map, packageSearch);
			return;
		}

		if (name == null)
			name = "";
		else
			name += ".";

		// add subpackages
		File[] dirs = file.listFiles(DIRECTORIES_ONLY);
		for (int i = 0; i < dirs.length; i++) {
			String pack = name + dirs[i].getName();
//			System.out.println("jar package " + pack);
			if (pack.startsWith(packageSearch)){ // /
				try {
					// add the present package
					map.put(new URL("file://" + dirs[i].getCanonicalPath()), pack);
				} catch (IOException ioe) {
					return;
				}
			}
			include(pack, dirs[i], map, packageSearch);
		}
	}

	private void includeJar(File file, Map<URL, String> map, String packageSearch) {
		if (file.isDirectory())
			return;

		URL jarURL = null;
		JarFile jar = null;
		try {
			jarURL = new URL("file:/" + file.getCanonicalPath());
			jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
			JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
			jar = conn.getJarFile();
		} catch (Exception e) {
			// not a JAR or disk I/O error
			// either way, just skip
			return;
		}

		if (jar == null || jarURL == null)
			return;

		// include the jar's "default" package (i.e. jar's root)
		//map.put(jarURL, "");

		Enumeration<JarEntry> e = jar.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();

			if (entry.isDirectory()) {
				if (entry.getName().toUpperCase().equals("META-INF/"))
					continue;
				String pack = packageNameFor(entry);
				if (pack.startsWith(packageSearch)) {
					try {
						map.put(new URL(jarURL.toExternalForm() + entry.getName()), pack);
					} catch (MalformedURLException murl) {
						// whacky entry?
						continue;
					}
				}
			}
		}
	}

	private static String packageNameFor(JarEntry entry) {
		if (entry == null)
			return "";
		String s = entry.getName();
		if (s == null)
			return "";
		if (s.length() == 0)
			return s;
		if (s.startsWith("/"))
			s = s.substring(1, s.length());
		if (s.endsWith("/"))
			s = s.substring(0, s.length() - 1);
		return s.replace('/', '.');
	}

	private final void includeResourceLocations(String packageName, Map<URL, String> map) {
		try {
			Enumeration<URL> resourceLocations = ClassFinder.class.getClassLoader().getResources(getPackagePath(packageName));

			while (resourceLocations.hasMoreElements()) {
				map.put(resourceLocations.nextElement(), packageName);
			}
		} catch (Exception e) {
			// well, we tried
			errors.add(e);
			return;
		}
	}

	private final Vector<String> findNotCompileGWTclass(String packageName, Map<URL, String> locations) {
		Vector<String> v = new Vector<String>();

		Vector<String> w = null; // new Vector<JClassType> ();

		// Package [] packages = Package.getPackages ();
		// for (int i=0;i<packages.length;i++)
		// {
		// System.out.println ("package: " + packages[i]);
		// }

		Iterator<URL> it = locations.keySet().iterator();
		while (it.hasNext()) {
			URL url = it.next();
			// System.out.println (url + "-->" + locations.get (url));

			w = findNotCompileGWTclass(url, locations.get(url));
			if (w != null && (w.size() > 0))
				v.addAll(w);
		}

		return v;
	}

	private final Vector<String> findNotCompileGWTclass(URL location, String packageName) {
		// System.out.println ("looking in package:" + packageName);
		// System.out.println ("looking for  class:" + superClass);

		synchronized (results) {

			// hash guarantees unique names...
			Map<String, URL> thisResult = new TreeMap<String, URL>(CLASS_COMPARATOR);
			Vector<String> v = new Vector<String>(); // ...but return a vector

			List<URL> knownLocations = new ArrayList<URL>();
			knownLocations.add(location);
			// TODO: add getResourceLocations() to this list

			// iterate matching package locations...
			for (int loc = 0; loc < knownLocations.size(); loc++) {
				URL url = knownLocations.get(loc);

				// Get a File object for the package
				File directory = new File(url.getFile());

				// System.out.println ("\tlooking in " + directory);

				if (directory.exists()) {
					// Get the list of the files contained in the package
					String[] files = directory.list();
					for (int i = 0; i < files.length; i++) {
						// we are only interested in .class files
						if (files[i].endsWith(".java")) {
							// removes the .class extension
							String classname = packageName + "." + files[i].substring(0, files[i].length() - 5);

							// System.out.println ("\t\tchecking file " + classname);

							// try {
							// JClassType c = Class.forName(packageName + "." + classname);
							// if (superClass.isAssignableFrom(c) && !fqcn.equals(packageName + "." + classname)) {
							// thisResult.put(c, url);
							// }
							// } catch (ClassNotFoundException cnfex) {
							// errors.add(cnfex);
							// // System.err.println(cnfex);
							// } catch (Exception ex) {
							// errors.add(ex);
							// // System.err.println (ex);
							// }
							JClassType c = typeOracle.findType(classname);
							if (c == null) {
								thisResult.put(classname, url);
							}
						}
					}
				} else {
					try {
						// It does not work with the filesystem: we must
						// be in the case of a package contained in a jar file.
						JarURLConnection conn = (JarURLConnection) url.openConnection();
						// String starts = conn.getEntryName();
						JarFile jarFile = conn.getJarFile();

						// System.out.println ("starts=" + starts);
						// System.out.println ("JarFile=" + jarFile);

						Enumeration<JarEntry> e = jarFile.entries();
						while (e.hasMoreElements()) {
							JarEntry entry = e.nextElement();
							String entryname = entry.getName();

							// System.out.println ("\tconsidering entry: " + entryname);

							if (!entry.isDirectory() && entryname.endsWith(".java")) {
								String classname = entryname.substring(0, entryname.length() - 5);
								if (classname.startsWith("/"))
									classname = classname.substring(1);
								classname = classname.replace('/', '.');

								// System.out.println ("\t\ttesting classname: " + classname);

								// TODO: verify this block
								JClassType c = typeOracle.findType(classname);
								if (c == null) {
									thisResult.put(classname, url);
								}
							}
						}
					} catch (IOException ioex) {
						// System.err.println(ioex);
						errors.add(ioex);
					}
				}
			} // while

			// System.out.println ("results = " + thisResult);

			results.putAll(thisResult);

			Iterator<String> it = thisResult.keySet().iterator();
			while (it.hasNext()) {
				v.add(it.next());
			}
			return v;

		} // synch results
	}

	private final static String getPackagePath(String packageName) {
		// Translate the package name into an "absolute" path
		String path = new String(packageName);
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		path = path.replace('.', '/');

		// ending with "/" indicates a directory to the classloader
		if (!path.endsWith("/"))
			path += "/";

		// for actual classloader interface (NOT Class.getResource() which
		// hacks up the request string!) a resource beginning with a "/"
		// will never be found!!! (unless it's at the root, maybe?)
		if (path.startsWith("/"))
			path = path.substring(1, path.length());

		// System.out.println ("package path=" + path);

		return path;
	}

	public static void main(String[] args) {
		// args = new String[] {"rcm.core.Any_String"};

		// ClassFinder finder = null;
		// Vector<JClassType> v = null;
		// List<Throwable> errors = null;
		//
		// if (args.length == 1) {
		// finder = new ClassFinder();
		// v = finder.findSubclasses(args[0]);
		// errors = finder.getErrors();
		// } else {
		// System.out.println("Usage: java ClassFinder <fully.qualified.superclass.name>");
		// return;
		// }
		//
		// System.out.println("RESULTS:");
		// if (v != null && v.size() > 0) {
		// for (JClassType cls : v) {
		// System.out.println(cls + " in " + ((finder != null) ? String.valueOf(finder.getLocationOf(cls)) : "?"));
		// }
		// } else {
		// System.out.println("No subclasses of " + args[0] + " found.");
		// }

		// TODO: verbose mode
		// if (errors != null && errors.size () > 0)
		// {
		// System.out.println ("ERRORS:");
		// for (Throwable t : errors) System.out.println (t);
		// }
	}
}