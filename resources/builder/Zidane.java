package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Zidane extends _Default{
	public Zidane() {
		super("Zidane", "tribal");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8340};
				break;
			case HP:
				ids = new int[]{8341};
				break;
			case S1:
				ids = new int[]{8327, 8328};
				break;
			case S2:
				ids = new int[]{8331, 8332};
				break;
			case EX:
				ids = new int[]{8338, 8339};
				break;
			case AA:
				ids = new int[]{5358};
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