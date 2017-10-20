package com.example.bradmoeller.myapplication.data;

public abstract class AbsVersionedObject implements VersionedObject {

    private int mMajorVersion = 0;
    private int mMinorVersion = 0;
    private int mPatchVersion = 0;
    private int mBuildVersion = 0;

    protected void setObjectVersion(int majorVersion, int minorVersion, int patchVersion, int buildVersion) {
        mMajorVersion = majorVersion;
        mMinorVersion = minorVersion;
        mPatchVersion = patchVersion;
        mBuildVersion = buildVersion;
    }

    @Override
    public int getVersionCode() {
        return (mMajorVersion * 10000) + (mMinorVersion * 1000) + (mPatchVersion * 100) + mBuildVersion;
    }

    @Override
    public String getVersionName() {
        return String.format("%s.%s.%s.%s", String.valueOf(mMajorVersion), String.valueOf(mMinorVersion), String.valueOf(mPatchVersion), String.valueOf(mBuildVersion));
    }

}
