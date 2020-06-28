package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cait_Sith extends _Default{
	public Cait_Sith() {
		super("Cait Sith", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait", "cait");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5709};
				break;
			case HP:
				ids = new int[]{5711};
				break;
			case S1:
				ids = new int[]{5701, 5700, 5699};
				break;
			case S2:
				ids = new int[]{2540, 2539, 2538};
				break;
			case EX:
				ids = new int[]{5715};
				break;
			case AA:
				ids = new int[]{5726};
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