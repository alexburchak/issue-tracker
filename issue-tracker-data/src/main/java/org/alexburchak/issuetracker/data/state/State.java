package org.alexburchak.issuetracker.data.state;

/**
 * @author alexburchak
 */
public enum State {
    STARTED,
    AUTHENTICATION,
    MENU,
    LISTING_ISSUES,
    ASKING_ISSUE_NAME,
    ASKING_ISSUE_DESCRIPTION,
    ASKING_ISSUE_PHOTO
}
