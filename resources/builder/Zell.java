package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Zell extends _Default{
	public Zell() {
		super("Zell", "dincht");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5169};
				break;
			case HP:
				ids = new int[]{5199, 5170};
				break;
			case S1:
				ids = new int[]{5159, 5160};
				break;
			case S2:
				ids = new int[]{5165, 5164};
				break;
			case EX:
				ids = new int[]{5168};
				break;
			case AA:
				ids = new int[]{5176};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}