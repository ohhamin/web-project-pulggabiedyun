from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.select import Select
from selenium.common.exceptions import NoSuchElementException
import time
from pprint import pprint
import pandas as pd

browser = webdriver.Chrome("chromedriver.exe")

browser.get("https://www.comcbt.com/cbt/modeselect.php?hack_number=0&h_db=k3")
time.sleep(0.5)

btn1 = browser.find_element(By.CSS_SELECTOR, "body > table:nth-child(9) > tbody > tr:nth-child(2) > td:nth-child(3) > form > input[type=submit]:nth-child(3)")
btn1.click()

select = Select(browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr:nth-child(1) > td > form > p > select"))
select.select_by_value("한국사능력검정시험")

btn2 = browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr:nth-child(1) > td > form > p > input[type=submit]:nth-child(4)")
btn2.click()

select = Select(browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr > td > form > p > select"))
select.select_by_value("k3")

btn2 = browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr > td > form > p > input[type=submit]:nth-child(4)")
btn2.click()

# radio = browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr > td > form > p > span:nth-child(2) > input[type=radio]:nth-child(9)")
# radio.click()

date_select = Select(browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr > td > form > p > select"))
btn3 = browser.find_element(By.CSS_SELECTOR, "body > table > tbody > tr > td > form > p > input[type=submit]:nth-child(13)")
quizList = []
for i in range(1, 41):
    date_select.select_by_index(i)
    btn3.send_keys(Keys.LEFT_CONTROL+"\n")
    browser.switch_to.window(browser.window_handles[-1])
    time.sleep(0.5)
    for j in range(1, 41) :
        quiz_XPATH = f"/html/body/table[{j}]/tbody/tr/td/"
        title = "table[1]/tbody/tr/td[2]/b"
        img = "img"
        bogi1 = "table[2]/tbody/tr/td[2]"
        bogi2 = "table[3]/tbody/tr/td[2]"
        bogi3 = "table[4]/tbody/tr/td[2]"
        bogi4 = "table[5]/tbody/tr/td[2]"
        answer = "font[1]/div"
        quiz = {}
        quiz["title"] = browser.find_element(By.XPATH, quiz_XPATH+title).text[:-5]
        quiz["answer"] = browser.find_element(By.XPATH, quiz_XPATH+answer).text
        try :
            quiz["img"] = browser.find_element(By.XPATH, quiz_XPATH+img).get_property("src")
        except NoSuchElementException :
            quiz["img"] = "None"

        if browser.find_element(By.XPATH, quiz_XPATH+bogi1).text == '':
            quiz["bogi1"] = browser.find_element(By.XPATH, quiz_XPATH+bogi1+"/img").get_property("src")
            quiz["bogi2"] = browser.find_element(By.XPATH, quiz_XPATH+bogi2+"/img").get_property("src")
            quiz["bogi3"] = browser.find_element(By.XPATH, quiz_XPATH+bogi3+"/img").get_property("src")
            quiz["bogi4"] = browser.find_element(By.XPATH, quiz_XPATH+bogi4+"/img").get_property("src")

        else :        
            quiz["bogi1"] = browser.find_element(By.XPATH, quiz_XPATH+bogi1).text
            quiz["bogi2"] = browser.find_element(By.XPATH, quiz_XPATH+bogi2).text
            quiz["bogi3"] = browser.find_element(By.XPATH, quiz_XPATH+bogi3).text
            quiz["bogi4"] = browser.find_element(By.XPATH, quiz_XPATH+bogi4).text
        pprint(quiz)
        quizList.append(quiz)
        # json_val = json.dumps(quiz)
        # pprint(json_val)
        # quizList.append(json_val)

    #탭닫고 첫탭으로 변경
    browser.close()
    browser.switch_to.window(browser.window_handles[0])

# pprint(quizList)
df = pd.DataFrame(quizList)
# print(df.head())
df.to_csv('quiz.csv', encoding="utf-8-sig", mode='w', index=False)




