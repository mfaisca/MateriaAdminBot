package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kefka extends _Default{
	public Kefka() {
		super("Kefka", "palazzo", "palazzo");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8436, 8438};
				break;
			case HP:
				ids = new int[]{8437};
				break;
			case S1:
				ids = new int[]{3581};
				break;
			case S2:
				ids = new int[]{3583};
				break;
			case EX:
				ids = new int[]{8434};
				break;
			case AA:
				ids = new int[]{6665};
				break;
			case LD:
				ids = new int[]{9299};
				break;
			case BT:
				ids = new int[]{9300};
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