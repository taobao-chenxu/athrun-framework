/**
 * 
 */
package org.athrun.android.result.kelude;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author taichan
 * 
 */
@XStreamAlias("results")
public class Results {
	@XStreamImplicit
	List<Result> results = new ArrayList<Result>();

	public void add(Result r) {
		results.add(r);
	}
}
