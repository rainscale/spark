import sqlite3
import pandas as pd


def create_table():
    # 连接到数据库
    conn = sqlite3.connect('words.db')
    cursor = conn.cursor()

    # 创建数据表
    cursor.execute('''
    create table if not exists words_from_excel(
        frequency integer,
        word text,
        pronunciation text,
        meaning text,
        sentence text)''')

    # 提交更改并关闭连接
    conn.commit()
    conn.close()


def insert_data_from_excel(file_path):
    # 读取Excel文件
    df = pd.read_excel(file_path)

    # 连接到数据库
    conn = sqlite3.connect('words.db')
    cursor = conn.cursor()

    # 插入数据到数据库
    for index, row in df.iterrows():
        cursor.execute(
            'insert into words_from_excel (frequency, word, pronunciation, meaning, sentence) values (?, ?, ?, ?, ?)',
            (row['频次'], row['单词'], row['音标'], row['释义'], row['例句']))

    # 提交更改并关闭连接
    conn.commit()
    conn.close()


def main():
    create_table()
    excel_file_path = 'source/words.xlsx'
    insert_data_from_excel(excel_file_path)
    print('Data has been imported from Excel to Sqlite3 database successfully.')


if __name__ == '__main__':
    main()