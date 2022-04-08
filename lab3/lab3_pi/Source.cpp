//������� 8
#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <stdio.h>
#include <stdint.h>
#include <windows.h>

//#define _CRT_SECURE_NO_WARNINGS

using namespace std;

BITMAPFILEHEADER head;//��������� � ����������� � �����
BITMAPINFOHEADER info;//��������� � ����������� �� �����������
RGBQUAD pixel;//��������� �������

//������� ���������� ������ ����� ���������� � ����� �������
void hide_byte_into_pixel(RGBQUAD* pixel, uint8_t hide_byte)

{
	//01001100
	//������� 2 �������� ���� FC = 11111100 �� ���� ������� �
	pixel->rgbBlue &= (0xFC);
	//���������� 2 ��������� ���� �� ���� ������� ���, 3 = 00000011
	pixel->rgbBlue |= (hide_byte >> 6) & 0x3;
	//�������
	pixel->rgbGreen &= (0xFC);
	//���������� ��� ������������ �� hide_byte
	pixel->rgbGreen |= (hide_byte >> 4) & 0x3;
	pixel->rgbRed &= (0xFC);
	pixel->rgbRed |= (hide_byte >> 2) & 0x3;
	pixel->rgbReserved &= (0xFC);
	pixel->rgbReserved |= (hide_byte) & 0x3;

}

//������������ ����� �� �������
uint8_t decode(RGBQUAD pixel)
{

	uint8_t byte;//������������� ����
	//��������� ��� ��������� �����
	byte = pixel.rgbBlue & 0x3;
	// ������� ����� ��� ����������� ��������� ���� 2 �����
	byte = byte << 2;
	byte |= pixel.rgbGreen & 0x3;

	byte = byte << 2;
	byte |= pixel.rgbRed & 0x3;

	byte = byte << 2;
	byte |= pixel.rgbReserved & 0x3;

	return byte;
}

int main()
{
	setlocale(LC_ALL, "Russian");
	bool flag = false;
	int choose = -1;
	while (true)
	{
		do
		{
			cout << "����� ������������� ���������� ������� 1" << endl;
			cout << "����� ������������ ���������� ������� 2" << endl;
			cout << "����� ����� ������� 0" << endl;
			cin >> choose;
		} while (choose <= -1 || choose >= 3);

		switch (choose)
		{
		case 0: 
			return 0;
		case 1:
			flag = true;
			break;
		case 2:
			flag = false;
			break;
		}


		if (flag) 
		{
			string filename;
			cout << "������� ��� ����� ��� ������������" << endl;
			cin >> filename;
			FILE* picture = fopen(filename.c_str(), "rb");//�������� ����� � �������� ������ ��� ������ 
			FILE* text = fopen("decode.txt", "wb");

			fread(&head, sizeof(head), 1, picture);//��������� ���������, ����� �������� �������
			fread(&info, sizeof(info), 1, picture);//��������� ���������� ��������

			while (true) 
			{
				fread(&pixel, sizeof(pixel), 1, picture);//��������� �� �������� 1 �������
				uint8_t byte = decode(pixel);

				if (byte == 0xFF)//����� ������, ����� ���������� �������������� �����
					break;

				fwrite(&byte, 1, 1, text);//����� �� ���� ���� ���������� � ��� ���
			}

			fclose(picture);
			fclose(text);

		}

		else 
		{

			FILE* picture = fopen("pic.bmp", "rb");//��������� �������� ���� ��������
			fread(&head, sizeof(head), 1, picture);
			fread(&info, sizeof(info), 1, picture);
			FILE* text = fopen("to_code.txt", "rb");//���� � ������� ��� �����������
			FILE* newPicture = fopen("result.bmp", "wb");//��������� ���� .bmp ��� ���������� �����������
			fwrite(&head, sizeof(head), 1, newPicture);
			fwrite(&info, sizeof(info), 1, newPicture);
			//while (true)
			int size = info.biWidth * info.biHeight;//������ ����� ���������� �������� � ��������

			for (int i = 0; i < size; i++)
			{
				//��������� � ������ ���� ���� �������
				uint8_t byte;//���� ��� ����������
				size_t x = fread(&byte, 1, 1, text);
				if (x != 1) byte = 0xFF;//���� ������ �� �������(����� �����), �� ����� ����� �������������� ���������
				fread(&pixel, sizeof(pixel), 1, picture);
				hide_byte_into_pixel(&pixel, byte);
				fwrite(&pixel, sizeof(pixel), 1, newPicture);
			}

			fclose(picture);
			fclose(text);
			fclose(newPicture);

		}
	}
	return 0;

}