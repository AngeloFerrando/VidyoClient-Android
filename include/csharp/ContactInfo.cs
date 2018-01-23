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
	public class ContactInfo{
#if __IOS__
		const string importLib = "libVidyoClient";
#else
		const string importLib = "libVidyoClient";
#endif
		private IntPtr objPtr; // opaque VidyoContactInfo reference.
		public IntPtr GetObjectPtr(){
			return objPtr;
		}
		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetemailsNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetemailsArrayNative(IntPtr obj, ref int size);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern void VidyoContactInfoFreeemailsArrayNative(IntPtr obj, int size);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetgroupsNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetgroupsArrayNative(IntPtr obj, ref int size);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern void VidyoContactInfoFreegroupsArrayNative(IntPtr obj, int size);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGethandleNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetidNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetnameNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetnicknameNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGetphotoNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGettelephonesNative(IntPtr obj);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern IntPtr VidyoContactInfoGettelephonesArrayNative(IntPtr obj, ref int size);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern void VidyoContactInfoFreetelephonesArrayNative(IntPtr obj, int size);

		[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
		private static extern ulong VidyoContactInfoGettimestampNative(IntPtr obj);

		public class ContactInfoProperty{
#if __IOS__
			const string importLib = "libVidyoClient";
#else
			const string importLib = "libVidyoClient";
#endif
			private IntPtr objPtr; // opaque VidyoContactInfoProperty reference.
			public IntPtr GetObjectPtr(){
				return objPtr;
			}
			[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
			private static extern IntPtr VidyoContactInfoPropertyGettypesNative(IntPtr obj);

			[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
			private static extern IntPtr VidyoContactInfoPropertyGettypesArrayNative(IntPtr obj, ref int size);

			[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
			private static extern void VidyoContactInfoPropertyFreetypesArrayNative(IntPtr obj, int size);

			[DllImport(importLib, CallingConvention = CallingConvention.Cdecl)]
			private static extern IntPtr VidyoContactInfoPropertyGetvalueNative(IntPtr obj);

			public List<String> types;
			public String value;
			public ContactInfoProperty(IntPtr obj){
				objPtr = obj;

				List<String> csTypes = new List<String>();
				int nTypesSize = 0;
				IntPtr nTypes = VidyoContactInfoPropertyGettypesArrayNative(VidyoContactInfoPropertyGettypesNative(objPtr), ref nTypesSize);
				int nTypesIndex = 0;
				while (nTypesIndex < nTypesSize) {
					csTypes.Add(Marshal.PtrToStringAnsi(Marshal.ReadIntPtr(nTypes + (nTypesIndex * Marshal.SizeOf(nTypes)))));
					nTypesIndex++;
				}

				types = csTypes;
				value = Marshal.PtrToStringAnsi(VidyoContactInfoPropertyGetvalueNative(objPtr));
				VidyoContactInfoPropertyFreetypesArrayNative(nTypes, nTypesSize);
			}
		};
		public List<ContactInfoProperty> emails;
		public List<String> groups;
		public String handle;
		public String id;
		public String name;
		public String nickname;
		public String photo;
		public List<ContactInfoProperty> telephones;
		public ulong timestamp;
		public ContactInfo(IntPtr obj){
			objPtr = obj;

			List<ContactInfoProperty> csEmails = new List<ContactInfoProperty>();
			int nEmailsSize = 0;
			IntPtr nEmails = VidyoContactInfoGetemailsArrayNative(VidyoContactInfoGetemailsNative(objPtr), ref nEmailsSize);
			int nEmailsIndex = 0;
			while (nEmailsIndex < nEmailsSize) {
				ContactInfoProperty csTemails = new ContactInfoProperty(Marshal.ReadIntPtr(nEmails + (nEmailsIndex * Marshal.SizeOf(nEmails))));
				csEmails.Add(csTemails);
				nEmailsIndex++;
			}

			List<String> csGroups = new List<String>();
			int nGroupsSize = 0;
			IntPtr nGroups = VidyoContactInfoGetgroupsArrayNative(VidyoContactInfoGetgroupsNative(objPtr), ref nGroupsSize);
			int nGroupsIndex = 0;
			while (nGroupsIndex < nGroupsSize) {
				csGroups.Add(Marshal.PtrToStringAnsi(Marshal.ReadIntPtr(nGroups + (nGroupsIndex * Marshal.SizeOf(nGroups)))));
				nGroupsIndex++;
			}

			List<ContactInfoProperty> csTelephones = new List<ContactInfoProperty>();
			int nTelephonesSize = 0;
			IntPtr nTelephones = VidyoContactInfoGettelephonesArrayNative(VidyoContactInfoGettelephonesNative(objPtr), ref nTelephonesSize);
			int nTelephonesIndex = 0;
			while (nTelephonesIndex < nTelephonesSize) {
				ContactInfoProperty csTtelephones = new ContactInfoProperty(Marshal.ReadIntPtr(nTelephones + (nTelephonesIndex * Marshal.SizeOf(nTelephones))));
				csTelephones.Add(csTtelephones);
				nTelephonesIndex++;
			}

			emails = csEmails;
			groups = csGroups;
			handle = Marshal.PtrToStringAnsi(VidyoContactInfoGethandleNative(objPtr));
			id = Marshal.PtrToStringAnsi(VidyoContactInfoGetidNative(objPtr));
			name = Marshal.PtrToStringAnsi(VidyoContactInfoGetnameNative(objPtr));
			nickname = Marshal.PtrToStringAnsi(VidyoContactInfoGetnicknameNative(objPtr));
			photo = Marshal.PtrToStringAnsi(VidyoContactInfoGetphotoNative(objPtr));
			telephones = csTelephones;
			timestamp = VidyoContactInfoGettimestampNative(objPtr);
			VidyoContactInfoFreetelephonesArrayNative(nTelephones, nTelephonesSize);
			VidyoContactInfoFreegroupsArrayNative(nGroups, nGroupsSize);
			VidyoContactInfoFreeemailsArrayNative(nEmails, nEmailsSize);
		}
	};
}
