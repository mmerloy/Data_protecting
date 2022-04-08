//вариант 8
#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <stdio.h>
#include <stdint.h>
#include <windows.h>

//#define _CRT_SECURE_NO_WARNINGS

using namespace std;

BITMAPFILEHEADER head;//структура с информацией о файле
BITMAPINFOHEADER info;//структура с информацией об изображении
RGBQUAD pixel;//структура пикселя

//функция шифрования одного байта информации в одном пикселе
void hide_byte_into_pixel(RGBQUAD* pixel, uint8_t hide_byte)

{
	//01001100
	//Убираем 2 младшиих бита FC = 11111100 за счет условия и
	pixel->rgbBlue &= (0xFC);
	//записываем 2 последних бита за счет условия или, 3 = 00000011
	pixel->rgbBlue |= (hide_byte >> 6) & 0x3;
	//очищаем
	pixel->rgbGreen &= (0xFC);
	//записываем два незаписанных из hide_byte
	pixel->rgbGreen |= (hide_byte >> 4) & 0x3;
	pixel->rgbRed &= (0xFC);
	pixel->rgbRed |= (hide_byte >> 2) & 0x3;
	pixel->rgbReserved &= (0xFC);
	pixel->rgbReserved |= (hide_byte) & 0x3;

}

//дешифрование байта из пикселя
uint8_t decode(RGBQUAD pixel)
{

	uint8_t byte;//зашифрованный байт
	//считываем два последних байта
	byte = pixel.rgbBlue & 0x3;
	// сделали сдвиг для возможности сохранить след 2 байта
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
			cout << "Чтобы раскодировать информацию введите 1" << endl;
			cout << "Чтобы закодировать информацию введите 2" << endl;
			cout << "Чтобы выйти введите 0" << endl;
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
			cout << "Введите имя файла для раскодировки" << endl;
			cin >> filename;
			FILE* picture = fopen(filename.c_str(), "rb");//открытие файла в бинарном режиме для чтения 
			FILE* text = fopen("decode.txt", "wb");

			fread(&head, sizeof(head), 1, picture);//считываем заголовок, чтобы сместить каретку
			fread(&info, sizeof(info), 1, picture);//считываем информацию картинки

			while (true) 
			{
				fread(&pixel, sizeof(pixel), 1, picture);//считываем из картинки 1 пиксель
				uint8_t byte = decode(pixel);

				if (byte == 0xFF)//чтобы понять, когда закончится закодированный текст
					break;

				fwrite(&byte, 1, 1, text);//иначе мы этот байт закидываем в тот тхт
			}

			fclose(picture);
			fclose(text);

		}

		else 
		{

			FILE* picture = fopen("pic.bmp", "rb");//открываем исходный файл картинки
			fread(&head, sizeof(head), 1, picture);
			fread(&info, sizeof(info), 1, picture);
			FILE* text = fopen("to_code.txt", "rb");//файл с текстом для кодирования
			FILE* newPicture = fopen("result.bmp", "wb");//открываем файл .bmp для шифрования информациии
			fwrite(&head, sizeof(head), 1, newPicture);
			fwrite(&info, sizeof(info), 1, newPicture);
			//while (true)
			int size = info.biWidth * info.biHeight;//узнаем общее количество пикселей в картинке

			for (int i = 0; i < size; i++)
			{
				//считываем и прячем пока есть символы
				uint8_t byte;//байт для шифрования
				size_t x = fread(&byte, 1, 1, text);
				if (x != 1) byte = 0xFF;//если чтение не удалось(конец файла), то ставм конец зашифрованного сообщения
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