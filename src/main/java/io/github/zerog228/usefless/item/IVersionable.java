package io.github.zerog228.usefless.item;

/**
 * Represents items that can have multiple versions/revisions
 * */
public interface IVersionable {

    String IVERSION_TAG = "custom_item_version";

    int getVersion();
}
