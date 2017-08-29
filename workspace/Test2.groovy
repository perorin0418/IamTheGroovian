
import static com.codeborne.selenide.Condition.*
import static com.codeborne.selenide.Selenide.*

import org.openqa.selenium.By

import spock.lang.Specification

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.WebDriverRunner

class Test extends Specification {

	def "length of Spock's and his friends' names"() {

		given:
		Configuration.browser = WebDriverRunner.CHROME;
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		// Googleトップページ
		// "selenide"を検索
		open("https://www.google.co.jp/");
		$("#lst-ib").val("selenide").pressEnter();

		when:
		// 検索ページ
		// Selenideの公式ページをクリック
		$(By.linkText("Selenide: concise UI tests in Java")).click();

		then:
		// Selenide公式ページ
		// 「What is Selenide?」という文言があることを確認
		$("body").shouldHave(text("What is Selenide?"));

	}
}
