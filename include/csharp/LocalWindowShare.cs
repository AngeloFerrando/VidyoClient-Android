// DO NOT EDIT! This is an autogenerated file. All changes will be
// overwritten!

//	Copyright (c) 2016 Vidyo, Inc. All rights reserved.


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;
using System.Runtime.InteropServices;

namespace VidyoClient
{
	public class LocalWindowShare{
#if __IOS__
		const string importLib = "libVidyoClient";
#else
		const string importLib = "libVidyoClient";
#endif
		private IntPtr objPtr; // opaque VidyoLocalWindowShare reference.
		public IntPtr GetObjectPtr(){
			return objPtr;
		}
		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern ulong VidyoLocalWindowShareAddToLocalRendererNative(IntPtr w, IntPtr renderer);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareAddToRemoteRendererNative(IntPtr w, IntPtr remoteRenderer);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern void VidyoLocalWindowShareClearConstraintsNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoLocalWindowShareConstructCopyNative(IntPtr other);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern void VidyoLocalWindowShareDestructNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareGetApplicationIconFrameDataUriAsyncNative(IntPtr w, ulong maxWidth, ulong maxHeight, OnApplicationIconDataUriComplete onComplete);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoLocalWindowShareGetApplicationNameNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoLocalWindowShareGetIdNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoLocalWindowShareGetNameNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareGetPreviewFrameDataUriAsyncNative(IntPtr w, ulong maxWidth, ulong maxHeight, OnPreviewDataUriComplete onComplete);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern uint VidyoLocalWindowShareGetProcessIdNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareIsApplicationNameSetNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareIsNameSetNative(IntPtr w);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareRemoveFromLocalRendererNative(IntPtr w, IntPtr renderer);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareRemoveFromRemoteRendererNative(IntPtr w, IntPtr remoteRenderer);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetBoundsConstraintsNative(IntPtr w, ulong maxFrameInterval, ulong minFrameInterval, uint maxWidth, uint minWidth, uint maxHeight, uint minHeight);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetCapabilityNative(IntPtr w, ulong frameInterval, Boolean showCursor, Boolean redactOccludedAreas);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetCursorVisibilityNative(IntPtr w, Boolean showCursor);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetDiscrerteConstraintsNative(IntPtr w, ulong maxFrameInterval, ulong minFrameInterval, uint width, uint height, double scaleFactor);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetFrameIntervalNative(IntPtr w, ulong frameInterval);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetPositionInLocalRendererNative(IntPtr w, IntPtr localRenderer, int x, int y, uint width, uint height, ulong frameInterval);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetRedactOccludedAreasNative(IntPtr w, Boolean redactOccludedAreas);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoLocalWindowShareSetScaleFactorNative(IntPtr w, double scaleFactor);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		public static extern IntPtr VidyoLocalWindowShareGetUserDataNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		public static extern void VidyoLocalWindowShareSetUserDataNative(IntPtr obj, IntPtr userData);

		[UnmanagedFunctionPointer(CallingConvention.Cdecl)]
		private delegate void OnApplicationIconDataUriComplete(IntPtr w, IntPtr icon);
		private static OnApplicationIconDataUriComplete _mOnApplicationIconDataUriComplete;
		[UnmanagedFunctionPointer(CallingConvention.Cdecl)]
		private delegate void OnPreviewDataUriComplete(IntPtr w, IntPtr videoFrame, LocalWindowShareState state);
		private static OnPreviewDataUriComplete _mOnPreviewDataUriComplete;
		public enum LocalWindowShareState{
			LocalwindowsharestateOk,
			LocalwindowsharestateNotVisible,
			LocalwindowsharestateMinimized,
			LocalwindowsharestateClosed,
			LocalwindowsharestateMiscError
		}
		public interface IGetApplicationIconFrameDataUriAsync{

			void OnApplicationIconDataUriComplete(String icon);
		}
		public interface IGetPreviewFrameDataUriAsync{

			void OnPreviewDataUriComplete(String videoFrame, LocalWindowShareState state);
		}
		private static IGetApplicationIconFrameDataUriAsync _mIGetApplicationIconFrameDataUriAsync;
		private static IGetPreviewFrameDataUriAsync _mIGetPreviewFrameDataUriAsync;
		public LocalWindowShare(IntPtr other){
			objPtr = VidyoLocalWindowShareConstructCopyNative(other);
			VidyoLocalWindowShareSetUserDataNative(objPtr, GCHandle.ToIntPtr(GCHandle.Alloc(this, GCHandleType.Weak)));
		}
		~LocalWindowShare(){
			if(objPtr != IntPtr.Zero){
				VidyoLocalWindowShareSetUserDataNative(objPtr, IntPtr.Zero);
				VidyoLocalWindowShareDestructNative(objPtr);
			}
		}
		public ulong AddToLocalRenderer(LocalRenderer renderer){

			ulong ret = VidyoLocalWindowShareAddToLocalRendererNative(objPtr, (renderer != null) ? renderer.GetObjectPtr():IntPtr.Zero);

			return ret;
		}
		public Boolean AddToRemoteRenderer(RemoteRenderer remoteRenderer){

			Boolean ret = VidyoLocalWindowShareAddToRemoteRendererNative(objPtr, (remoteRenderer != null) ? remoteRenderer.GetObjectPtr():IntPtr.Zero);

			return ret;
		}
		public void ClearConstraints(){

			VidyoLocalWindowShareClearConstraintsNative(objPtr);
		}
		public Boolean GetApplicationIconFrameDataUriAsync(ulong maxWidth, ulong maxHeight, IGetApplicationIconFrameDataUriAsync _iIGetApplicationIconFrameDataUriAsync){
			_mIGetApplicationIconFrameDataUriAsync = _iIGetApplicationIconFrameDataUriAsync;
			_mOnApplicationIconDataUriComplete = OnApplicationIconDataUriCompleteDelegate;

			Boolean ret = VidyoLocalWindowShareGetApplicationIconFrameDataUriAsyncNative(objPtr, maxWidth, maxHeight, _mOnApplicationIconDataUriComplete);

			return ret;
		}
		public String GetApplicationName(){

			IntPtr ret = VidyoLocalWindowShareGetApplicationNameNative(objPtr);

			return Marshal.PtrToStringAnsi(ret);
		}
		public String GetId(){

			IntPtr ret = VidyoLocalWindowShareGetIdNative(objPtr);

			return Marshal.PtrToStringAnsi(ret);
		}
		public String GetName(){

			IntPtr ret = VidyoLocalWindowShareGetNameNative(objPtr);

			return Marshal.PtrToStringAnsi(ret);
		}
		public Boolean GetPreviewFrameDataUriAsync(ulong maxWidth, ulong maxHeight, IGetPreviewFrameDataUriAsync _iIGetPreviewFrameDataUriAsync){
			_mIGetPreviewFrameDataUriAsync = _iIGetPreviewFrameDataUriAsync;
			_mOnPreviewDataUriComplete = OnPreviewDataUriCompleteDelegate;

			Boolean ret = VidyoLocalWindowShareGetPreviewFrameDataUriAsyncNative(objPtr, maxWidth, maxHeight, _mOnPreviewDataUriComplete);

			return ret;
		}
		public uint GetProcessId(){

			uint ret = VidyoLocalWindowShareGetProcessIdNative(objPtr);

			return ret;
		}
		public Boolean IsApplicationNameSet(){

			Boolean ret = VidyoLocalWindowShareIsApplicationNameSetNative(objPtr);

			return ret;
		}
		public Boolean IsNameSet(){

			Boolean ret = VidyoLocalWindowShareIsNameSetNative(objPtr);

			return ret;
		}
		public Boolean RemoveFromLocalRenderer(LocalRenderer renderer){

			Boolean ret = VidyoLocalWindowShareRemoveFromLocalRendererNative(objPtr, (renderer != null) ? renderer.GetObjectPtr():IntPtr.Zero);

			return ret;
		}
		public Boolean RemoveFromRemoteRenderer(RemoteRenderer remoteRenderer){

			Boolean ret = VidyoLocalWindowShareRemoveFromRemoteRendererNative(objPtr, (remoteRenderer != null) ? remoteRenderer.GetObjectPtr():IntPtr.Zero);

			return ret;
		}
		public Boolean SetBoundsConstraints(ulong maxFrameInterval, ulong minFrameInterval, uint maxWidth, uint minWidth, uint maxHeight, uint minHeight){

			Boolean ret = VidyoLocalWindowShareSetBoundsConstraintsNative(objPtr, maxFrameInterval, minFrameInterval, maxWidth, minWidth, maxHeight, minHeight);

			return ret;
		}
		public Boolean SetCapability(ulong frameInterval, Boolean showCursor, Boolean redactOccludedAreas){

			Boolean ret = VidyoLocalWindowShareSetCapabilityNative(objPtr, frameInterval, showCursor, redactOccludedAreas);

			return ret;
		}
		public Boolean SetCursorVisibility(Boolean showCursor){

			Boolean ret = VidyoLocalWindowShareSetCursorVisibilityNative(objPtr, showCursor);

			return ret;
		}
		public Boolean SetDiscrerteConstraints(ulong maxFrameInterval, ulong minFrameInterval, uint width, uint height, double scaleFactor){

			Boolean ret = VidyoLocalWindowShareSetDiscrerteConstraintsNative(objPtr, maxFrameInterval, minFrameInterval, width, height, scaleFactor);

			return ret;
		}
		public Boolean SetFrameInterval(ulong frameInterval){

			Boolean ret = VidyoLocalWindowShareSetFrameIntervalNative(objPtr, frameInterval);

			return ret;
		}
		public Boolean SetPositionInLocalRenderer(LocalRenderer localRenderer, int x, int y, uint width, uint height, ulong frameInterval){

			Boolean ret = VidyoLocalWindowShareSetPositionInLocalRendererNative(objPtr, (localRenderer != null) ? localRenderer.GetObjectPtr():IntPtr.Zero, x, y, width, height, frameInterval);

			return ret;
		}
		public Boolean SetRedactOccludedAreas(Boolean redactOccludedAreas){

			Boolean ret = VidyoLocalWindowShareSetRedactOccludedAreasNative(objPtr, redactOccludedAreas);

			return ret;
		}
		public Boolean SetScaleFactor(double scaleFactor){

			Boolean ret = VidyoLocalWindowShareSetScaleFactorNative(objPtr, scaleFactor);

			return ret;
		}
#if __IOS__
[ObjCRuntime.MonoPInvokeCallback(typeof(OnApplicationIconDataUriComplete))]
#endif
		private static void OnApplicationIconDataUriCompleteDelegate(IntPtr w, IntPtr icon){
			if(_mIGetApplicationIconFrameDataUriAsync != null)
				_mIGetApplicationIconFrameDataUriAsync.OnApplicationIconDataUriComplete(Marshal.PtrToStringAnsi(icon));
		}
#if __IOS__
[ObjCRuntime.MonoPInvokeCallback(typeof(OnPreviewDataUriComplete))]
#endif
		private static void OnPreviewDataUriCompleteDelegate(IntPtr w, IntPtr videoFrame, LocalWindowShareState state){
			if(_mIGetPreviewFrameDataUriAsync != null)
				_mIGetPreviewFrameDataUriAsync.OnPreviewDataUriComplete(Marshal.PtrToStringAnsi(videoFrame), state);
		}
	};
}
