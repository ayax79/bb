package com.blackbox.foundation.bookmark;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public enum WishStatus {

    WISHED,
    WISHED_BY,
    MUTUAL,
    NEITHER

}
