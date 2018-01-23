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
	public class Calendar{
#if __IOS__
		const string importLib = "libVidyoClient";
#else
		const string importLib = "libVidyoClient";
#endif
		private IntPtr objPtr; // opaque VidyoCalendar reference.
		public IntPtr GetObjectPtr(){
			return objPtr;
		}
		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarConnectNative(IntPtr c, IntPtr connProperties);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoCalendarConstructCopyNative(IntPtr other);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarCreateMeetingNative(IntPtr c, IntPtr feedback, IntPtr meeting, [MarshalAs(UnmanagedType.LPStr)]String createToken);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern void VidyoCalendarDestructNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoCalendarGetIdNative(IntPtr c);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarGetMeetingsByDayNative(IntPtr c, uint year, uint month, uint day, [MarshalAs(UnmanagedType.LPStr)]String createToken);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarGetMeetingsByMonthNative(IntPtr c, uint year, uint month, [MarshalAs(UnmanagedType.LPStr)]String createToken);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarGetMeetingsByWeekNative(IntPtr c, uint year, uint month, uint day, [MarshalAs(UnmanagedType.LPStr)]String createToken);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		[return: MarshalAs(UnmanagedType.I4)]
		private static extern CalendarState VidyoCalendarGetServiceStateNative(IntPtr c);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoCalendarGetServiceTypeNative(IntPtr c);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarRemoveMeetingNative(IntPtr c, IntPtr feedback, IntPtr meeting, [MarshalAs(UnmanagedType.LPStr)]String createToken);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarSearchMeetingsNative(IntPtr c, [MarshalAs(UnmanagedType.LPStr)]String searchText, [MarshalAs(UnmanagedType.LPStr)]String beginDate, [MarshalAs(UnmanagedType.LPStr)]String endDate, uint index, uint pageSize);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarSetRoomIdMatchStringNative(IntPtr c, [MarshalAs(UnmanagedType.LPStr)]String regexp);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern Boolean VidyoCalendarUpdateMeetingNative(IntPtr c, IntPtr feedback, IntPtr meeting, [MarshalAs(UnmanagedType.LPStr)]String createToken);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		public static extern IntPtr VidyoCalendarGetUserDataNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		public static extern void VidyoCalendarSetUserDataNative(IntPtr obj, IntPtr userData);

		public Calendar(IntPtr other){
			objPtr = VidyoCalendarConstructCopyNative(other);
			VidyoCalendarSetUserDataNative(objPtr, GCHandle.ToIntPtr(GCHandle.Alloc(this, GCHandleType.Weak)));
		}
		~Calendar(){
			if(objPtr != IntPtr.Zero){
				VidyoCalendarSetUserDataNative(objPtr, IntPtr.Zero);
				VidyoCalendarDestructNative(objPtr);
			}
		}
		public Boolean Connect(List<FormField> connProperties){

			IntPtr nConnProperties = IntPtr.Zero;

			Boolean ret = VidyoCalendarConnectNative(objPtr, nConnProperties);

			return ret;
		}
		public Boolean CreateMeeting(CalendarFeedback feedback, Meeting meeting, String createToken){

			Boolean ret = VidyoCalendarCreateMeetingNative(objPtr, (feedback != null) ? feedback.GetObjectPtr():IntPtr.Zero, (meeting != null) ? meeting.GetObjectPtr():IntPtr.Zero, createToken);

			return ret;
		}
		public String GetId(){

			IntPtr ret = VidyoCalendarGetIdNative(objPtr);

			return Marshal.PtrToStringAnsi(ret);
		}
		public Boolean GetMeetingsByDay(uint year, uint month, uint day, String createToken){

			Boolean ret = VidyoCalendarGetMeetingsByDayNative(objPtr, year, month, day, createToken);

			return ret;
		}
		public Boolean GetMeetingsByMonth(uint year, uint month, String createToken){

			Boolean ret = VidyoCalendarGetMeetingsByMonthNative(objPtr, year, month, createToken);

			return ret;
		}
		public Boolean GetMeetingsByWeek(uint year, uint month, uint day, String createToken){

			Boolean ret = VidyoCalendarGetMeetingsByWeekNative(objPtr, year, month, day, createToken);

			return ret;
		}
		public CalendarState GetServiceState(){

			CalendarState ret = VidyoCalendarGetServiceStateNative(objPtr);

			return ret;
		}
		public String GetServiceType(){

			IntPtr ret = VidyoCalendarGetServiceTypeNative(objPtr);

			return Marshal.PtrToStringAnsi(ret);
		}
		public Boolean RemoveMeeting(CalendarFeedback feedback, Meeting meeting, String createToken){

			Boolean ret = VidyoCalendarRemoveMeetingNative(objPtr, (feedback != null) ? feedback.GetObjectPtr():IntPtr.Zero, (meeting != null) ? meeting.GetObjectPtr():IntPtr.Zero, createToken);

			return ret;
		}
		public Boolean SearchMeetings(String searchText, String beginDate, String endDate, uint index, uint pageSize){

			Boolean ret = VidyoCalendarSearchMeetingsNative(objPtr, searchText, beginDate, endDate, index, pageSize);

			return ret;
		}
		public Boolean SetRoomIdMatchString(String regexp){

			Boolean ret = VidyoCalendarSetRoomIdMatchStringNative(objPtr, regexp);

			return ret;
		}
		public Boolean UpdateMeeting(CalendarFeedback feedback, Meeting meeting, String createToken){

			Boolean ret = VidyoCalendarUpdateMeetingNative(objPtr, (feedback != null) ? feedback.GetObjectPtr():IntPtr.Zero, (meeting != null) ? meeting.GetObjectPtr():IntPtr.Zero, createToken);

			return ret;
		}
	};
}
