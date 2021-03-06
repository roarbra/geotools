/**
 */
package org.w3._2001.smil20.language.impl;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.smil20.language.AnimateColorType;
import org.w3._2001.smil20.language.AnimateMotionType;
import org.w3._2001.smil20.language.AnimateType;
import org.w3._2001.smil20.language.DocumentRoot;
import org.w3._2001.smil20.language.LanguagePackage;
import org.w3._2001.smil20.language.SetType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getAnimate <em>Animate</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getAnimateColor <em>Animate Color</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getAnimateMotion <em>Animate Motion</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.impl.DocumentRootImpl#getSet <em>Set</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DocumentRootImpl extends MinimalEObjectImpl.Container implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xSISchemaLocation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return LanguagePackage.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, LanguagePackage.DOCUMENT_ROOT__MIXED);
        }
        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EMap<String, String> getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap<>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, LanguagePackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EMap<String, String> getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap<>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, LanguagePackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AnimateType getAnimate() {
        return (AnimateType)getMixed().get(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnimate(AnimateType newAnimate, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE, newAnimate, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAnimate(AnimateType newAnimate) {
        ((FeatureMap.Internal)getMixed()).set(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE, newAnimate);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AnimateColorType getAnimateColor() {
        return (AnimateColorType)getMixed().get(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE_COLOR, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnimateColor(AnimateColorType newAnimateColor, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE_COLOR, newAnimateColor, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAnimateColor(AnimateColorType newAnimateColor) {
        ((FeatureMap.Internal)getMixed()).set(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE_COLOR, newAnimateColor);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AnimateMotionType getAnimateMotion() {
        return (AnimateMotionType)getMixed().get(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE_MOTION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnimateMotion(AnimateMotionType newAnimateMotion, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE_MOTION, newAnimateMotion, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAnimateMotion(AnimateMotionType newAnimateMotion) {
        ((FeatureMap.Internal)getMixed()).set(LanguagePackage.Literals.DOCUMENT_ROOT__ANIMATE_MOTION, newAnimateMotion);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SetType getSet() {
        return (SetType)getMixed().get(LanguagePackage.Literals.DOCUMENT_ROOT__SET, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSet(SetType newSet, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(LanguagePackage.Literals.DOCUMENT_ROOT__SET, newSet, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSet(SetType newSet) {
        ((FeatureMap.Internal)getMixed()).set(LanguagePackage.Literals.DOCUMENT_ROOT__SET, newSet);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case LanguagePackage.DOCUMENT_ROOT__MIXED:
                return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
            case LanguagePackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case LanguagePackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE:
                return basicSetAnimate(null, msgs);
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_COLOR:
                return basicSetAnimateColor(null, msgs);
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_MOTION:
                return basicSetAnimateMotion(null, msgs);
            case LanguagePackage.DOCUMENT_ROOT__SET:
                return basicSetSet(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case LanguagePackage.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case LanguagePackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case LanguagePackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE:
                return getAnimate();
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_COLOR:
                return getAnimateColor();
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_MOTION:
                return getAnimateMotion();
            case LanguagePackage.DOCUMENT_ROOT__SET:
                return getSet();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case LanguagePackage.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case LanguagePackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case LanguagePackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE:
                setAnimate((AnimateType)newValue);
                return;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_COLOR:
                setAnimateColor((AnimateColorType)newValue);
                return;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_MOTION:
                setAnimateMotion((AnimateMotionType)newValue);
                return;
            case LanguagePackage.DOCUMENT_ROOT__SET:
                setSet((SetType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case LanguagePackage.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case LanguagePackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case LanguagePackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE:
                setAnimate((AnimateType)null);
                return;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_COLOR:
                setAnimateColor((AnimateColorType)null);
                return;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_MOTION:
                setAnimateMotion((AnimateMotionType)null);
                return;
            case LanguagePackage.DOCUMENT_ROOT__SET:
                setSet((SetType)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case LanguagePackage.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case LanguagePackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case LanguagePackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE:
                return getAnimate() != null;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_COLOR:
                return getAnimateColor() != null;
            case LanguagePackage.DOCUMENT_ROOT__ANIMATE_MOTION:
                return getAnimateMotion() != null;
            case LanguagePackage.DOCUMENT_ROOT__SET:
                return getSet() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
